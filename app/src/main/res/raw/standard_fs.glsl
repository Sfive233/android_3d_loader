#version 300 es
precision mediump float;
layout (location = 0) out vec4 FragColor;
layout (location = 1) out vec4 HDRColor;

uniform sampler2D DiffuseMap;
uniform sampler2D SpecularMap;
uniform sampler2D NormalMap;
uniform sampler2D HeightMap;
uniform sampler2D ShadowMap;
uniform vec3 AmbientLightColor;
uniform vec3 DiffuseLightColor;
uniform vec3 SpecularLightColor;
uniform vec3 ObjectColor;
uniform vec3 ObjectSpecular;
uniform float Shininess;
uniform bool IsUseLighting;
uniform bool IsHasNormalMap;
uniform bool IsUseDiffuseMap;
uniform bool IsUseSpecularMap;
uniform bool IsHasHeightMap;
uniform bool IsUseHeightMapHalfRange;
uniform bool IsDiscardHeightMapEdge;
uniform float HeightScale;
uniform float MaxLayerNum;
uniform float MinLayerNum;
uniform bool IsUseShadowMap;
uniform float Bias;
uniform bool IsUseSoftShadow;
uniform int SoftShadowSampleNum;
uniform bool IsUseGammaCorrection;

const float Gamma = 2.2;

in vec3 FragPos;
in vec2 SourceTexCoords;

in vec3 TanLightDir;
in vec3 TanViewPos;
in vec3 TanFragPos;

in vec3 SourceLightDir;
in vec3 SourceViewPos;
in vec3 SourceFragPos;
in vec3 SourceNormal;

in vec4 LightSpaceFragPos;

float VdotN(vec3 vec, vec3 normal);
vec2 ParallaxMapping(vec2 texCoords, vec3 viewDir);
vec2 SteepParallaxMapping(vec2 texCoords, vec3 viewDir);
vec2 ParallaxOcclusionMapping(vec2 texCoords, vec3 viewDir);
float shadowCalculation();
void main(){
    vec3 outPut;

    if (IsUseLighting){

        // 视线、光线、纹理坐标、法线
        vec3 viewDir;
        vec3 lightDir;
        vec3 normal;
        vec2 texcoords = SourceTexCoords;
        // 使用切线空间（法线贴图）
        if(IsHasNormalMap){
            // 将范围从[0, 1]转为[-1, 1]
            viewDir = normalize(TanViewPos - TanFragPos);
            lightDir = normalize(-TanLightDir);
            // 是否使用高度贴图
            if(IsHasHeightMap){// todo 使用ak模型时视差映射会崩 可能是返回的texcoords有问题
                float val = texture(HeightMap, texcoords).r;
                float scale = 0.1f;
                vec2 p = viewDir.xy / viewDir.z * (val * scale);
                texcoords = ParallaxOcclusionMapping(texcoords, viewDir);
                if(IsDiscardHeightMapEdge && (texcoords.x >= 1.0f || texcoords.x <= 0.0f || texcoords.y >= 1.0f || texcoords.y <= 0.0f)){
                    discard;
                }
            }
            // [0.0, 1.0]转[-1.0, 1.0]
            normal = normalize(texture(NormalMap, texcoords).rgb * 2.0 - 1.0);
        }else{
            viewDir = normalize(SourceViewPos - SourceFragPos);
            lightDir = normalize(-SourceLightDir);
            normal = normalize(SourceNormal);
        }

        // 环境光照和物体颜色
        vec3 ambient;
        vec3 objectColor;
        // 使用材质贴图
        if(IsUseDiffuseMap){

            if(true){
                objectColor = pow(texture(DiffuseMap, texcoords).rgb, vec3(2.2));
            }else {
                objectColor = texture(DiffuseMap, texcoords).rgb;
            }

            if(texture(DiffuseMap, texcoords).a <= 0.5){
                discard;
            }
            ambient = AmbientLightColor * objectColor;
        }else{
            objectColor = ObjectColor;
            ambient = AmbientLightColor * objectColor;
        }

        // 物体高光
        vec3 objectSpecular;
        // 使用高光贴图
        if(IsUseSpecularMap){
            objectSpecular = texture(SpecularMap, texcoords).rgb;
        }else{
            objectSpecular = ObjectSpecular;
        }

        // 漫反射光照
        float diff = VdotN(normal, lightDir);
        vec3 diffuse = DiffuseLightColor * diff * objectColor;

        // 镜面光照
        vec3 halfWayDir = normalize(lightDir + viewDir);
        float spec = pow(VdotN(halfWayDir, normal), Shininess);
        vec3 specular = SpecularLightColor * spec * objectSpecular;

        // 阴影
        float shadowFactor = IsUseShadowMap ? shadowCalculation() : 0.0;

        vec3 result = vec3(ambient + (diffuse + specular) * (1.0 - shadowFactor));

        // MRT
        float colorBrightness = dot(result, vec3(0.2126, 0.7152, 0.0722));
        if(colorBrightness > 1.0){
            HDRColor = vec4(result, 1.0);
        }
        FragColor = vec4(result, 1.0);
    }else {
        FragColor = vec4(texture(DiffuseMap, SourceTexCoords).rgb, 1.0);
    }
}
float VdotN(vec3 vec, vec3 normal){
    float dotResult = dot(vec, normal);
//    return clamp(dotResult, 0.0, 1.0);
    return max(dotResult, 0.0);
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
    if(!IsUseHeightMapHalfRange){
        currentSampleDepth = (1.0 - texture(HeightMap, currentTexCoords).r) * 2.0 - 1.0;
    }else{
        currentSampleDepth = 1.0 - texture(HeightMap, currentTexCoords).r;
    }

    while(currentLayerDepth < currentSampleDepth){
        currentTexCoords -= pPerLayer;

        if(!IsUseHeightMapHalfRange){
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
    if(!IsUseHeightMapHalfRange){
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
float shadowCalculation(){
    vec3 shadowMapFragCoords = LightSpaceFragPos.xyz / LightSpaceFragPos.w;
    shadowMapFragCoords = shadowMapFragCoords * 0.5 + 0.5;

    if(shadowMapFragCoords.x >= 1.0f || shadowMapFragCoords.x <= 0.0f || shadowMapFragCoords.y >= 1.0f || shadowMapFragCoords.y <= 0.0f){
        return 0.0f;
    }

    if(IsUseSoftShadow){
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