#version 300 es
precision mediump float;
out vec4 FragColor;


uniform sampler2D DiffuseMap;
uniform sampler2D AmbientMap;
uniform sampler2D SpecularMap;
uniform vec3 DiffuseColor;
uniform vec3 AmbientColor;
uniform vec3 SpecularColor;

in vec3 FragPos;
in vec2 TexCoords;
in vec3 Normal;

void main(){
    FragColor = texture(AmbientMap, vec2(TexCoords.x, 1.0f - TexCoords.y));
}
