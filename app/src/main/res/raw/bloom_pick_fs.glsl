#version 300 es
precision mediump float;
out vec4 FragColor;

uniform sampler2D SourceImage;
uniform float Exposure;

in vec3 FragPos;
in vec2 TexCoords;

vec3 ACESToneMapping(vec3 source);
void main(){
    vec3 sourceColor = texture(SourceImage, TexCoords).rgb;
    vec3 result = sourceColor;
    result = ACESToneMapping(result);
    if(dot(result, vec3(0.2126, 0.7152, 0.0722)) >= 1.0){
        FragColor = vec4(sourceColor, 1.0);
    }
//    FragColor = vec4(result, 1.0);

}
vec3 ACESToneMapping(vec3 source){
    float A = 2.51f;
    float B = 0.03f;
    float C = 2.43f;
    float D = 0.59f;
    float E = 0.14f;

    source *= Exposure;
    return (source * (A * source + B)) / (source * (C * source + D) + E);
}