#version 150

#moj_import <matrix.glsl>

uniform sampler2D Sampler0;
uniform sampler2D Sampler1;

uniform float GameTime;
uniform int PortalLayers;

in vec4 texProj0;
in vec4 vertexColor;

const mat4 SCALE_TRANSLATE = mat4(
    0.5, 0.0, 0.0, 0.5,
    0.0, 0.5, 0.0, 0.5,
    0.0, 0.0, 1.0, 0.0,
    0.0, 0.0, 0.0, 1.0
);

mat4 portal_layer(float layer) {
    mat4 translate = mat4(
        1.0, 0.0, 0.0, sin(12.0 / layer * (GameTime * 20.0)),
        0.0, 1.0, 0.0, -(2.0 + layer / 0.5) * (GameTime * 75.0),
        0.0, 0.0, 1.0, 0.0,
        0.0, 0.0, 0.0, 1.0
    );

    return mat4(mat2((4.5 - layer / 4.0))) * translate * SCALE_TRANSLATE;
}

out vec4 fragColor;

void main() {
    vec4 color = textureProj(Sampler0, texProj0) * vec4(vertexColor[0], vertexColor[1], vertexColor[2], 0.5);
    for (int i = 0; i < PortalLayers; i++) {
        color += textureProj(Sampler1, texProj0 * portal_layer(float(i + 1))) * vec4(vertexColor[0], vertexColor[1], vertexColor[2], 1/(i+1));
    }
    fragColor = color;
}