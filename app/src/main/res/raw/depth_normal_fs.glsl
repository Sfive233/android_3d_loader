#version 300 es
precision mediump float;
layout (location = 0) out vec4 GPositionDepth;
layout (location = 1) out vec3 GNormal;

uniform float Near;
uniform float Far;

in vec3 FragPos;
in vec3 Normal;

float LinearizedDepth(float depth);
void main(){
    GPositionDepth.xyz = FragPos;
    GPositionDepth.w = LinearizedDepth(gl_FragCoord.z);

    GNormal = normalize(Normal);
}
float LinearizedDepth(float depth){
    float z = depth * 2.0 - 1.0;
    float linearDepth = (2.0 * Near * Far) / (Far + Near - z * (Far - Near));
    return linearDepth;
}