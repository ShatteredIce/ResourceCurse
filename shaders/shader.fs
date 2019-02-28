#version 120

uniform sampler2D sampler;
uniform float red;
uniform float green;
uniform float blue;

varying vec2 tex_coords;

void main() {

	vec4 tex = texture2D(sampler, tex_coords);
	gl_FragColor = tex + vec4(red, green, blue, 1)*tex.a;
	


}