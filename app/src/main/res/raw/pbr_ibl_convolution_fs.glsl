#version 300 es
precision highp float;
out vec3 FragColor;

uniform samplerCube CubeMap;
uniform float SampleDelta;

const float PI = 3.14159265359f;
in vec3 LocalPos;
in vec2 Texcoords;

void main(){
    vec3 irradiance = vec3(0.0f);

    vec3 Normal = normalize(LocalPos);
    vec3 Up = vec3(0.0, 1.0, 0.0);
    vec3 Right =  normalize(cross(Up, Normal));
    Up = normalize(cross(Normal, Right));

//    float sampleDelta = 0.025;
    float sampleDelta = SampleDelta;
    float sampleNum = 0.0;
    for(float phi = 0.0; phi < 2.0 * PI; phi += sampleDelta){// 偏航角，范围是一个圆
        for(float theta = 0.0; theta < 0.5 * PI; theta += sampleDelta){// 倾斜角，范围是四分之一个圆
            // 球面坐标转笛卡尔坐标(在Tangent空间)
            vec3 tangentSample = vec3(sin(theta) * cos(phi), sin(theta) * sin(phi), cos(theta));
            // Tangent空间转世界空间(相当于乘TBN矩阵)
            vec3 sampleVec = tangentSample.x * Right + tangentSample.y * Up + tangentSample.z * Normal;

            irradiance += texture(CubeMap, sampleVec).rgb * cos(theta) * sin(theta);
            sampleNum ++;
        }
    }

    irradiance = PI * irradiance * (1.0f / float(sampleNum));

    FragColor = irradiance;
}
