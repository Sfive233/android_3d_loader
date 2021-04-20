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

out vec3 Color;

void main(){
    FragPos = vec3(Model * vec4(aPos, 1.0));
    Normal = mat3(transpose(inverse(Model))) * aNormal;
    TexCoords = aTexCoord;

    Color = aPos;

    gl_Position = Projection * View * vec4(FragPos, 1.0);

    gl_PointSize = 10.0;
}
