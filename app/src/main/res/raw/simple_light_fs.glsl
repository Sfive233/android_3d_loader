#version 300 es
precision mediump float;
out vec4 FragColor;


uniform vec3 LightDirection;
uniform vec3 LightColor;
uniform vec3 AmbientColor;

in vec3 FragPos;
in vec2 TexCoords;
in vec3 Normal;

void main(){
    float diff = max(dot(-LightDirection, Normal), 0.0f);
    vec3 resultColor = AmbientColor * LightColor * diff;
    FragColor = vec4(resultColor, 1.0f);
}
