#version 300 es
precision mediump float;
out float FragColor;

void main(){
    float depth = gl_FragCoord.z;
    FragColor = depth;
}