#version 300 es
precision mediump float;
out vec3 FragColor;

uniform sampler2D PositionDepthMap;
uniform sampler2D NormalMap;
uniform sampler2D NoiseMap;
uniform mat4 ProjectionMatrix;
uniform int SampleNum;

uniform vec3 Samples[256];

in vec2 TexCoords;

void main(){

    // base input for ssao
    vec3 fragPos = texture(PositionDepthMap, TexCoords).rgb;
    vec3 normal = texture(NormalMap, TexCoords).rgb;
    vec2 noiseScale = vec2(textureSize(PositionDepthMap, 0).x / textureSize(NoiseMap, 0).x, textureSize(PositionDepthMap, 0).y / textureSize(NoiseMap, 0).y);
    vec3 randomVec = normalize(texture(NoiseMap, TexCoords * noiseScale).rgb);

    vec3 T = vec3(randomVec - normal * dot(normal, randomVec));
    vec3 B = cross(normal, T);
    mat3 TBN = mat3(T, B, normal);

    float occlusion = 0.0;
    float radius = 1.0;
    for(int i = 0; i < SampleNum; i++){
        vec3 ssaoSample = TBN * Samples[i];
        ssaoSample = fragPos + ssaoSample * radius;

        vec4 offset = vec4(ssaoSample, 1);

        offset = ProjectionMatrix * offset;
        offset.xyz /= offset.w;
        offset.xyz = offset.xyz * 0.5 + 0.5;

        float sampleDepth = -texture(PositionDepthMap, offset.xy).w;

        float rangeCheck = smoothstep(0.0, 1.0, radius / abs(sampleDepth - fragPos.z));
        occlusion += (sampleDepth >= ssaoSample.z ? 1.0: 0.0) * rangeCheck;
    }
    occlusion = 1.0 - (occlusion / float(SampleNum));

    FragColor = vec3(occlusion);
}
