#version 300 es
precision mediump float;
layout (location = 0) out vec4 FragColor;

uniform sampler2D AlbedoMap;
uniform sampler2D MetallicMap;
uniform sampler2D RoughnessMap;
uniform sampler2D AOMap;
uniform samplerCube DiffuseIrradianceMap;
uniform vec3 LightDirection;
uniform vec3 LightDiffuseColor;
uniform vec3 ViewPos;

in vec3 FragPos;
in vec2 TexCoords;
in vec3 Normal;

const float PI = 3.14159265359;
vec3 Albedo = vec3(0.5, 0.0, 0.0);
float Metallic = 0.5;
float Roughness = 0.5f;
float AO = 1.0f;

vec3 ViewDirection;
vec3 HalfVector;
vec3 F0;
vec3 F;
vec3 Kd;
vec3 Ks;

float GeometrySchlickGGX(float dotResult);
float GeometrySmith();
float TrowbridgeReitzGGX();
vec3 FresnelSchlickRoughness();
vec3 FresnelSchlick();
vec3 CookTorrance();
vec3 Lambert();
vec3 BRDF();
vec3 Radiance();
float NormalDot(vec3 vector);

void main(){
    ViewDirection = normalize(ViewPos - FragPos);
    HalfVector = normalize(ViewDirection + LightDirection);

    Albedo = pow(texture(AlbedoMap, TexCoords).rgb, vec3(2.2));
    Metallic = texture(MetallicMap, TexCoords).r;
    Roughness = texture(RoughnessMap, TexCoords).r;
    AO = texture(AOMap, TexCoords).r;

    vec3 L0;
    for(int i = 0; i < 1; i++){
        L0 += BRDF() * Radiance() * NormalDot(LightDirection);
    }

    vec3 ambient = vec3(0.03f) * Albedo * AO;

    vec3 finalColor = ambient + L0;

    FragColor = vec4(finalColor, 1.0);
}
vec3 BRDF(){
    Ks = FresnelSchlickRoughness();
    Kd = vec3(1.0) - Ks;
    Kd *= 1.0 - Metallic;

//    return Kd * Lambert() + CookTorrance();
    return Kd * Lambert() + max(CookTorrance(), vec3(0.0, 0.0, 0.0));
}
vec3 CookTorrance(){
    float D = TrowbridgeReitzGGX();
    F = FresnelSchlick();
    float G = GeometrySmith();

    vec3 num = D * F * G;
    float denum = 4.0 * max(NormalDot(ViewDirection) * NormalDot(LightDirection), 0.0);

    vec3 result = num / max(denum, 0.00000001);
    return result;
}
float GeometrySchlickGGX(float dotResult){
    float k = pow(Roughness + 1.0, 2.0) / 8.0;

    float num = dotResult;
    float denum = dotResult * (1.0 - k) + k;

    return num / max(denum, 0.0);
}
float GeometrySmith(){
    float subNVK = GeometrySchlickGGX(NormalDot(ViewDirection));
    float subNLK = GeometrySchlickGGX(NormalDot(LightDirection));

    return subNVK * subNLK;
}
float TrowbridgeReitzGGX(){
    float a = Roughness * Roughness;
    float a2 = a * a;

    float num = a2;
    float denum = PI * pow(pow(NormalDot(HalfVector), 2.0) * (a2 - 1.0) + 1.0, 2.0);

    return num / max(denum, 0.00000001);
}
vec3 FresnelSchlick(){

    vec3 F0 = vec3(0.04f);// 非金属物体可以默认0.04
    F0 = mix(F0, Albedo, Metallic);

    F = F0 + (1.0 - F0) * pow((1.0 - clamp(NormalDot(ViewDirection), 0.0, 1.0)), 5.0);
    return F;
}
vec3 FresnelSchlickRoughness(){

    vec3 F0 = vec3(0.04f);// 非金属物体可以默认0.04
    F0 = mix(F0, Albedo, Metallic);

    return F0 + (max(vec3(1.0 - Roughness), F0) - F0) * pow(1.0 - NormalDot(ViewDirection), 5.0);
}
vec3 Lambert(){
    return Albedo / PI;
}
float NormalDot(vec3 vector){
    return max(dot(Normal, vector), 0.0);
}
vec3 Radiance(){
    return LightDiffuseColor;
}