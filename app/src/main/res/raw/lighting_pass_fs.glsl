#version 300 es
precision mediump float;
out vec4 FragColor;

//in vec3 FragPos;
in vec2 TexCoords;
//in vec3 Normal;
in vec3 LightSourceDirection;

uniform sampler2D GPosition;
uniform sampler2D GAlbedo;
uniform sampler2D GNormal;
uniform sampler2D SSAOMap;
uniform vec3 LightDirection;
uniform vec3 AmbientLightColor;
uniform vec3 DiffuseLightColor;
uniform vec3 SpecularLightColor;

void main(){
    vec3 result = vec3(0.5);

    vec3 fragPos = texture(GPosition, TexCoords).rgb;
    vec3 normal = texture(GNormal, TexCoords).rgb;
    vec3 albedo = texture(GAlbedo, TexCoords).rgb;

    float occlusion = texture(SSAOMap, TexCoords).r;
//    vec3 ambient = vec3(0.3 * occlusion);
    vec3 ambient = AmbientLightColor * albedo;
    ambient *= occlusion;

    vec3 lightDir = normalize(vec3(1.0, 1.0, 1.0));
    vec3 diffuse = dot(normal, lightDir) * albedo * DiffuseLightColor;



    result = vec3(occlusion);

    FragColor = vec4(ambient, 1.0);
}
