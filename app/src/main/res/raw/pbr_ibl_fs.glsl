#version 300 es
precision highp float;
layout (location = 0) out vec3 FragColor;
layout (location = 1) out vec3 BrightColor;

uniform bool IsUseAlbedoMap;
uniform sampler2D AlbedoMap;
uniform vec3 AlbedoVal;

uniform bool IsUseMetallicMap;
uniform sampler2D MetallicMap;
uniform float MetallicVal;

uniform bool IsUseRoughnessMap;
uniform sampler2D RoughnessMap;
uniform float RoughnessVal;

uniform bool IsHasNormalMap;
uniform sampler2D NormalMap;

uniform sampler2D AOMap;
uniform bool IsUseAOMap;

uniform bool IsUseShadowMapping;
uniform sampler2D ShadowMap;
uniform int SoftShadowSampleNum;
uniform float Bias;

uniform bool IsHasHeightMap;
uniform sampler2D HeightMap;
uniform float MaxLayerNum;
uniform float MinLayerNum;
uniform float HeightScale;
uniform bool DiscardEdge;
uniform bool UseHalfHeightRange;

uniform samplerCube DiffuseIrradianceMap;
uniform samplerCube SpecularPreFilterMap;
uniform float MaxReflectionLod;
uniform sampler2D SpecularBRDFLut;

uniform vec3 LightDiffuseColor;
uniform vec3 ViewPos;

in vec3 FragPos;
in vec2 SourceTexCoords;
in vec3 SourceLightDir;
in vec3 SourceViewPos;
in vec3 SourceFragPos;
in vec3 SourceNormal;
in vec4 LightSpaceFragPos;

in vec3 TanLightDir;
in vec3 TanViewPos;
in vec3 TanFragPos;

const float PI = 3.14159265359;


float DistributionGGX(vec3 Normal, vec3 Half, float roughness){
    float a = roughness * roughness;
    float a2 = a * a;
    float NdotH = max(dot(Normal, Half), 0.0);
    float NdotH2 = NdotH * NdotH;

    float num = a2;
    float denum = (NdotH2 * (a2 - 1.0) + 1.0);
    denum = PI * denum * denum;

    return num / max(denum, 0.00000001);
}
float GeometrySchlickGGX(float dotResult, float roughness){
    float r = roughness + 1.0;
    float k = (r * r) / 8.0;

    float num = dotResult;
    float denum = dotResult * (1.0 - k) + k;

    return num / max(denum, 0.0);
}
float GeometrySmith(vec3 Normal, vec3 ViewDir, vec3 LightDir, float roughness){
    float NdotV = max(dot(Normal, ViewDir), 0.0);
    float NdotL = max(dot(Normal, LightDir), 0.0);
    float subNVK = GeometrySchlickGGX(NdotV, roughness);
    float subNLK = GeometrySchlickGGX(NdotL, roughness);

    return subNVK * subNLK;
}
vec3 FresnelSchlick(float cosTheta, vec3 F0){
    return F0 + (1.0 - F0) * pow((1.0 - cosTheta), 5.0);//
}
vec3 FresnelSchlickRoughness(float cosTheta, vec3 F0, float roughness){
    return F0 + (max(vec3(1.0 - roughness), F0) - F0) * pow(1.0 - cosTheta, 5.0);
}
vec3 Lambert();
vec3 Radiance();
float shadowCalculation();
vec2 ParallaxOcclusionMapping(vec2 texCoords, vec3 viewDir);

void main(){


    float shadowRate = 1.0;
    if(IsUseShadowMapping){// 阴影
        float shadowFactor = shadowCalculation();
        shadowRate = 1.0 - shadowFactor;
    }

    vec3 Normal = normalize(SourceNormal);
    vec2 TexCoords = SourceTexCoords;
    vec3 ViewDir = normalize(SourceViewPos - SourceFragPos);
    vec3 ReflectDir = reflect(-ViewDir, Normal);
    vec3 LightDir = normalize(-SourceLightDir);
    if(IsHasNormalMap){// 是否使用法线贴图
        ViewDir = normalize(TanViewPos - TanFragPos);
        LightDir = normalize(-TanLightDir);
        if(IsHasHeightMap){// 是否使用高度贴图
            float val = texture(HeightMap, TexCoords).r;
            float scale = 0.1f;
            vec2 p = ViewDir.xy / ViewDir.z * (val * scale);
            TexCoords = ParallaxOcclusionMapping(TexCoords, ViewDir);
            if(DiscardEdge && (TexCoords.x >= 1.0f || TexCoords.x <= 0.0f || TexCoords.y >= 1.0f || TexCoords.y <= 0.0f)){
                discard;
            }
        }
        Normal = normalize(texture(NormalMap, TexCoords).rgb * 2.0 - 1.0);
    }
    // 获取反照率、金属度、粗糙度和环境光遮蔽度
    vec3 Albedo = IsUseAlbedoMap? pow(texture(AlbedoMap, TexCoords).rgb, vec3(2.2)): AlbedoVal;
    float Metallic = IsUseMetallicMap? texture(MetallicMap, TexCoords).r: MetallicVal;
    float Roughness = IsUseRoughnessMap? texture(RoughnessMap, TexCoords).r: RoughnessVal;
    float AO = IsUseAOMap? texture(AOMap, TexCoords).r: 1.0;

    //PBR直接光照部分
    vec3 F0 = vec3(0.04);
    F0 = mix(F0, Albedo, Metallic);
    vec3 L0 = vec3(1.0);
    for(int i = 0; i < 1; i++){
        vec3 HalfVector = normalize(ViewDir + LightDir);
        vec3 radiance = LightDiffuseColor;

        float NDF = DistributionGGX(Normal, HalfVector, Roughness);
        float G = GeometrySmith(Normal, ViewDir, LightDir, Roughness);
        vec3 F = FresnelSchlick(max(dot(HalfVector, ViewDir), 0.0f), F0);

        vec3 num = NDF * G * F;
        float denum = 4.0 * max(dot(Normal, ViewDir), 0.0) * max(dot(Normal, LightDir), 0.0);
        vec3 specular = num / max(denum, 0.00000001);

        vec3 kS = F;
        vec3 kD = vec3(1.0) - kS;
        kD *= 1.0 - Metallic;

        float NdotL = max(dot(Normal, LightDir), 0.0);

        L0 = (kD * Albedo / PI + specular) * radiance * NdotL;
    }
    // PBF间接光照部分
    vec3 F = FresnelSchlickRoughness(max(dot(Normal, ViewDir), 0.0), F0, Roughness);
    vec3 kS = F;
    vec3 kD = 1.0 - kS;
    kD *= 1.0 - Metallic;
    // 间接光照漫反射
    vec3 irradiance = texture(DiffuseIrradianceMap, Normal).rgb;
    vec3 diffuse = irradiance * Albedo;
    // 间接光照镜面反射
    vec3 preFilterColor = textureLod(SpecularPreFilterMap, ReflectDir, Roughness * MaxReflectionLod).rgb;
    vec2 brdf = texture(SpecularBRDFLut, vec2(max(dot(Normal, ViewDir), 0.0), Roughness)).rg;
    vec3 specular = preFilterColor * (F * brdf.x + brdf.y);
    // 环境光照
    vec3 ambient = (kD * diffuse + specular * shadowRate) * AO;

    vec3 finalColor = ambient + L0 * shadowRate;

    FragColor = finalColor;

    float colorBrightness = dot(finalColor, vec3(0.2126, 0.7152, 0.0722));
    if(colorBrightness > 0.7){
        BrightColor = finalColor;
    }
}
float shadowCalculation(){
    vec3 shadowMapFragCoords = LightSpaceFragPos.xyz / LightSpaceFragPos.w;
    shadowMapFragCoords = shadowMapFragCoords * 0.5 + 0.5;

    if(shadowMapFragCoords.x >= 1.0f || shadowMapFragCoords.x <= 0.0f || shadowMapFragCoords.y >= 1.0f || shadowMapFragCoords.y <= 0.0f){
        return 0.0f;
    }

    if(true){
        int sampleNum = SoftShadowSampleNum;
        vec2 texelSize = 1.0 / vec2(float(textureSize(ShadowMap, 0)));
        float shadowFactors;
        int counter = 0;
        for(int i = -sampleNum; i < sampleNum; i++){
            for(int j = -sampleNum; j < sampleNum; j++){
                vec2 offsetCoord = vec2(i, j) * texelSize + shadowMapFragCoords.xy;
                float currentDepth = texture(ShadowMap, offsetCoord).r;
                shadowFactors += currentDepth < shadowMapFragCoords.z - Bias? 1.0: 0.0;
                counter++;
            }
        }
        return shadowFactors / float(counter);
    }else{
        float currentshadowMapDepth = texture(ShadowMap, shadowMapFragCoords.xy).r;
        float currentFragDepth = shadowMapFragCoords.z;
        return currentshadowMapDepth < currentFragDepth - Bias? 1.0: 0.0;
    }

}
vec2 ParallaxOcclusionMapping(vec2 texCoords, vec3 viewDir){
    float maxLayerNum = MaxLayerNum;
    float minLayerNum = MinLayerNum;
    // 根据viewDir和平面法线（这里暂时写死为0.0f, 0.0f, 1.0f）的夹角，动态生成样本数量
    float layerNum = mix(maxLayerNum, minLayerNum, abs(dot(vec3(0.0f, 0.0f, 1.0f), viewDir)));
    //    float heightScale = 0.02f;
    float heightScale = HeightScale;

    float depthPerLayer = 1.0f / layerNum;
    vec2 fullP = viewDir.xy / viewDir.z * 1.0f * heightScale;
    vec2 pPerLayer = fullP / layerNum;

    float currentLayerDepth = 0.0f;
    vec2 currentTexCoords = texCoords;
    float currentSampleDepth;
    if(UseHalfHeightRange){
        currentSampleDepth = (1.0 - texture(HeightMap, currentTexCoords).r) * 2.0 - 1.0;
    }else{
        currentSampleDepth = 1.0 - texture(HeightMap, currentTexCoords).r;
    }

    while(currentLayerDepth < currentSampleDepth){
        currentTexCoords -= pPerLayer;

        if(UseHalfHeightRange){
            currentSampleDepth = (1.0 - texture(HeightMap, currentTexCoords).r) * 2.0 - 1.0;
        }else{
            currentSampleDepth = 1.0 - texture(HeightMap, currentTexCoords).r;
        }

        currentLayerDepth += depthPerLayer;
    }

    // 插值
    vec2 texCoords0 = currentTexCoords;
    float sampleDepth0 = currentSampleDepth;
    float layerDepth0 = currentLayerDepth;
    vec2 texCoords1 = currentTexCoords + pPerLayer;
    float sampleDepth1;
    if(UseHalfHeightRange){
        sampleDepth1 = (1.0 - texture(HeightMap, texCoords1).r) * 2.0 - 1.0;
    }else{
        sampleDepth1 = 1.0 - texture(HeightMap, texCoords1).r;
    }
    float layerDepth1 = currentLayerDepth - depthPerLayer;
    float delta0 = layerDepth0 - sampleDepth0;
    float delta1 = sampleDepth1 - layerDepth1;
    float weight0 = delta0 / (delta0 + delta1);
    float weight1 = 1.0 - weight0;
    vec2 tc0 = texCoords0 * weight1;
    vec2 tc1 = texCoords1 * weight0;

    return tc0+tc1;
}