#version 300 es
layout (location = 0) in vec3 aPos;

out vec2 TexCoords;

//out vec3 FragPos;
//out vec3 Normal;
out vec3 LightSourceDirection;

//uniform sampler2D GPosition;
//uniform sampler2D GNormal;
uniform vec3 LightDirection;

void main(){
    TexCoords = vec2(aPos.x / 2.0 + 0.5, aPos.y / 2.0 + 0.5);
    gl_Position = vec4(aPos, 1.0);

//    FragPos = texture(GPosition, TexCoords).rgb;

//    vec3 normal = texture(GNormal, TexCoords).rgb;
//    Normal = normal;

    LightSourceDirection = normalize(LightDirection);
}
