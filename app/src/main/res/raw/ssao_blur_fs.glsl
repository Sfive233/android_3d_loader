#version 300 es
precision mediump float;
out float FragColor;

uniform sampler2D SourceImage;

in vec3 FragPos;
in vec2 TexCoords;

void main(){
    float result = 0.5;
    result = texture(SourceImage, TexCoords).r;

    vec2 texelSize = 1.0 / vec2(textureSize(SourceImage, 0));
    for(int x = -2; x < 2; x++){
        for(int y = -2; y < 2 ; y++){
            vec2 offset = vec2(float(x), float(y)) * texelSize;
            result += texture(SourceImage, TexCoords + offset).r;
        }
    }
    result /= 16.0;

    FragColor = result;
}
