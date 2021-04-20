#version 300 es
precision mediump float;
out vec3 FragColor;

uniform sampler2D BloomMap;
uniform sampler2D SourceMap;
uniform bool IsHorizontal;
uniform bool IsComplete;

in vec3 FragPos;
in vec2 TexCoords;

void main(){
    vec3 result;
    if(IsComplete){
        vec3 sourceColor = texture(SourceMap, TexCoords).rgb;
        vec3 bloomColor = texture(BloomMap, TexCoords).rgb;
        result = sourceColor + bloomColor;
    }else {
        int sampleNum = 6;
        float weight[6] = float[] (	0.198596,0.175713,0.121703,0.065984,0.028002,0.0093);
        float texel = 1.0 / float(textureSize(BloomMap, 0));
        result = texture(BloomMap, TexCoords).rgb * weight[0];
        if (IsHorizontal){
            for (int i = 1; i < sampleNum; i++){
                result += texture(BloomMap, TexCoords + vec2(texel * float(i), 0.0)).rgb * weight[i];
                result += texture(BloomMap, TexCoords - vec2(texel * float(i), 0.0)).rgb * weight[i];
            }
        } else {
            for (int i = 1; i < sampleNum; i++){
                result += texture(BloomMap, TexCoords + vec2(0.0, texel * float(i))).rgb * weight[i];
                result += texture(BloomMap, TexCoords - vec2(0.0, texel * float(i))).rgb * weight[i];
            }
        }
    }
    FragColor = result;
}
