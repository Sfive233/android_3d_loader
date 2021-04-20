#version 300 es
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec2 aTexCoord;
layout (location = 2) in vec3 aNormal;

uniform mat4 Model;
uniform mat4 View;
uniform mat4 Projection;

out vec3 FragPos;
out vec3 Normal;
out vec2 TexCoords;

void main(){

    vec4 viewPos = View * Model * vec4(aPos, 1.0);
    FragPos = viewPos.xyz;
    gl_Position = Projection * viewPos;
    TexCoords = aTexCoord;

    mat3 normalMatrix = transpose(inverse(mat3(View * Model)));
    Normal = normalMatrix * aNormal;

//    Normal = mat3(transpose(inverse(Model))) * normalize(aNormal);
}
