#version 300 es
precision mediump float;
out vec4 FragColor;

uniform vec3 Color;
uniform sampler2D Texture;
uniform bool IsShowPureColor;
uniform bool IsShowTexture;
uniform bool IsGammaCorrection;

in vec3 FragPos;
in vec2 TexCoords;

vec3 HDR2LDR(vec3 source);
vec3 GammaUp(vec3 source);
void main(){
    vec3 result = vec3(0.5);
    vec2 texcoords = vec2(TexCoords.x, TexCoords.y);
    result = texture(Texture, texcoords).rgb;
    if(IsGammaCorrection){
        result = GammaUp(result);
    }
    FragColor = vec4(result, 1.0);
}
vec3 GammaUp(vec3 source){
    float gamma = 2.2;
    return pow(source, vec3(1.0 / gamma));
}
