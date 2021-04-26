Android_3d_Loader

基于OpenGL ES 3.0的3D模型查看器，支持模型读取、材质替换、光照调节。

安装包路径：app/release/android_3d_loader.apk

使用到的项目

> TGAReader：[https://github.com/npedotnet/TGAReader]
> JavaHDR：[https://github.com/Ivelate/JavaHDR]

# 功能支持

- 模型
  - [x] .obj
  - [x] .stl
- 材质
  - [x] Diffuse Map
  - [x] Specular Map
  - [x] Normal Map
  - [x] Height Map
  - [x] PBR
- SkyBox
  - [x] .hdr
- 光照
  - [x] Direction Lighting
  - [x] Shadow Mapping
- 后期处理
  - [x] Bloom
  - [x] ACES Tone Mapping

# 效果展示

Direct Lighting：
![DirectLighting](http://sfive233.gitee.io/img/direct_lighting.png "DirectLighting")
模型读取：
![obj](http://sfive233.gitee.io/img/obj.gif "obj")

传统渲染：

![Traditional](http://sfive233.gitee.io/img/traditional.png "Traditional")

PBR渲染：
![PBR](http://sfive233.gitee.io/img/pbr.png "PBR")

天空盒：
![Skybox](http://sfive233.gitee.io/img/skybox.gif "Skybox")

色调映射：

![ToneMapping](http://sfive233.gitee.io/img/tone_mapping.gif "ToneMapping")