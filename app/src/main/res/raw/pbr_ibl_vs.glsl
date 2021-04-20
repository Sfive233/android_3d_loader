#version 300 es
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec2 aTexCoord;
layout (location = 2) in vec3 aNormal;
layout (location = 3) in vec3 aTangent;
layout (location = 4) in vec3 aBiTangent;

uniform mat4 Model;
uniform mat4 View;
uniform mat4 Projection;
uniform mat4 LightProjectionViewMatrix;

uniform vec3 LightDirection;
uniform vec3 ViewPos;

uniform bool IsHasNormalMap;

out vec3 FragPos;
out vec3 SourceLightDir;
out vec3 SourceViewPos;
out vec3 SourceFragPos;
out vec3 SourceNormal;
out vec2 SourceTexCoords;
out vec4 LightSpaceFragPos;

out vec3 TanLightDir;
out vec3 TanViewPos;
out vec3 TanFragPos;

void main(){
    FragPos = vec3(Model * vec4(aPos, 1.0));


    if(IsHasNormalMap){
        mat3 normalMatrix = transpose(inverse(mat3(Model)));
        vec3 T = normalize(vec3(normalMatrix * aTangent));
        vec3 B = normalize(vec3(normalMatrix * aBiTangent));
        vec3 N = normalize(vec3(normalMatrix * aNormal));
        mat3 TBN = mat3(T, B, N);
        mat3 inverseTBN = transpose(TBN);
        TanLightDir = inverseTBN * normalize(LightDirection);
        TanViewPos = inverseTBN * ViewPos;
        TanFragPos = inverseTBN * FragPos;
    }

    SourceNormal = mat3(transpose(inverse(Model))) * aNormal;
    SourceLightDir = normalize(LightDirection);
    SourceViewPos = ViewPos;
    SourceFragPos = FragPos;

    SourceTexCoords = aTexCoord;

    gl_Position = Projection * View * vec4(FragPos, 1.0);

    LightSpaceFragPos = LightProjectionViewMatrix * vec4(FragPos, 1.0);
}
