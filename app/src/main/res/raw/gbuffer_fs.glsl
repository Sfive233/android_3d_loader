#version 300 es
precision mediump float;
layout (location = 0) out vec4 GPositionDepth;
layout (location = 1) out vec3 GNormal;
layout (location = 2) out vec3 GColor;

uniform sampler2D Albedo;

in vec3 FragPos;
in vec2 TexCoords;
in vec3 Normal;

float LinearizedDepth(float depth);
vec3 GammaCorrection(vec3 source);
void main(){
    GPositionDepth.xyz = FragPos;
    GPositionDepth.w = LinearizeDepth(gl_FragCoord.z);

    GNormal = normalize(Normal);

    GColor = GammaCorrection(texture(Albedo, TexCoords).rgb);
}
float LinearizedDepth(float depth){
    const float near = 0.1;
    const float far = 100000.0;
    float z = depth * 2.0 - 1.0;
    float linearDepth = (2.0 * near * far) / (far + near - z * (far - near));
    return linearDepth;
}
vec3 GammaCorrection(vec3 source){
    return pow(source, vec3(2.2));
}