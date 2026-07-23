def scale_and_crop(sw, sh, nw, nh):
    scale = max(nw / sw, nh / sh)
    scaled_w = scale * sw
    scaled_h = scale * sh
    left = (nw - scaled_w) / 2.0
    top = (nh - scaled_h) / 2.0
    print(f"Scale: {scale}, Left: {left}, Top: {top}")
    print(f"Left edge maps to: {0 * scale + left}")
    print(f"Right edge maps to: {sw * scale + left}")
    print(f"Top edge maps to: {0 * scale + top}")
    print(f"Bottom edge maps to: {sh * scale + top}")

scale_and_crop(100, 200, 200, 200)
