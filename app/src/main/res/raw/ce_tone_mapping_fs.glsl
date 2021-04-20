#version 300 es
precision mediump float;
out vec4 FragColor;

uniform sampler2D SourceImage;
uniform float Exposure;

in vec3 FragPos;
in vec2 TexCoords;

vec3 CEToneMapping(vec3 source);
void main(){
    vec3 result = texture(SourceImage, TexCoords).rgb;
    result = CEToneMapping(result);
    FragColor = vec4(result, 1.0);
}
vec3 CEToneMapping(vec3 source){
    return vec3(1.0f) - exp(-source * Exposure);
}