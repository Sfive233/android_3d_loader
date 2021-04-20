#version 300 es
precision mediump float;
out vec3 FragColor;

in vec3 FragPos;
in vec2 TexCoords;

uniform sampler2D DepthMap;

vec2 Texel;

vec3 DepthToNormal(float depth);
void main(){
    Texel = vec2(1.0 / float(textureSize(DepthMap, 0).x), 1.0 / float(textureSize(DepthMap, 0).y));
    float depth = texture(DepthMap, TexCoords).r;

    FragColor = DepthToNormal(depth);
}
vec3 DepthToNormal(float depth){
    vec2 offset0 = vec2(0.0, Texel.y * 1.0);
    vec2 offset1 = vec2(Texel.x * 1.0, 0.0);
    float depth0 = texture(DepthMap, TexCoords + offset0).r;
    float depth1 = texture(DepthMap, TexCoords + offset1).r;

    vec3 normal;

    vec3 v0 = vec3(offset0, depth0 - depth);
    vec3 v1 = vec3(offset1, depth1 - depth);

    normal = cross(v0, v1);
    normal.z = -normal.z;
    normal = normalize(normal);
    return normal;
}