from pathlib import Path

import numpy as np
from PIL import Image, ImageDraw, ImageFilter


ROOT = Path(__file__).resolve().parents[1]
OUT = ROOT / "output" / "whiskey-cutouts"
OUT.mkdir(parents=True, exist_ok=True)

SOURCES = [
    (
        Path(r"C:\Users\knh47\Desktop\위스키 자료\KakaoTalk_20260523_180845444_01.jpg"),
        "hibiki_cutout.png",
        "hibiki_preview.png",
    ),
    (
        Path(r"C:\Users\knh47\Desktop\위스키 자료\KakaoTalk_20260523_220742181.jpg"),
        "black_bottle_cutout.png",
        "black_bottle_preview.png",
    ),
    (
        Path(r"C:\Users\knh47\Desktop\위스키 자료\KakaoTalk_20260523_220519102_02.jpg"),
        "royal_salute_cutout.png",
        "royal_salute_preview.png",
    ),
]


def poly(draw, points, fill=255):
    draw.polygon(points, fill=fill)


def ellipse(draw, box, fill=255):
    draw.ellipse(box, fill=fill)


def mask_hibiki(size):
    mask = Image.new("L", size, 0)
    d = ImageDraw.Draw(mask)

    # Visible bottle body, shoulder, neck and stopper.
    poly(
        d,
        [
            (690, 865),
            (965, 790),
            (1340, 790),
            (1600, 875),
            (1760, 1015),
            (1790, 2765),
            (1660, 2940),
            (765, 2940),
            (620, 2760),
            (615, 1070),
        ],
    )
    poly(d, [(860, 155), (1475, 130), (1480, 385), (1375, 500), (930, 500), (830, 390)])
    poly(d, [(935, 420), (1160, 425), (1105, 880), (860, 850), (875, 540)])
    poly(d, [(1235, 415), (1400, 455), (1490, 835), (1270, 880), (1250, 520)])

    # Remove the foreground hand and thumb that cross the bottle.
    poly(
        d,
        [
            (0, 555),
            (475, 530),
            (870, 390),
            (1115, 355),
            (1180, 500),
            (1030, 790),
            (830, 1040),
            (470, 1215),
            (0, 1190),
        ],
        0,
    )
    poly(d, [(1055, 345), (1265, 380), (1295, 825), (1175, 950), (1025, 800), (1000, 510)], 0)
    poly(d, [(0, 1040), (410, 1120), (410, 1385), (0, 1340)], 0)

    return mask


def mask_black_bottle(size):
    mask = Image.new("L", size, 0)
    d = ImageDraw.Draw(mask)

    poly(d, [(125, 790), (590, 720), (1580, 800), (1595, 1015), (585, 1095), (120, 1125)])
    poly(
        d,
        [
            (1515, 770),
            (1845, 600),
            (2200, 610),
            (3535, 615),
            (3825, 770),
            (3930, 1025),
            (3880, 1485),
            (3630, 1765),
            (2265, 1830),
            (1940, 1715),
            (1660, 1385),
            (1520, 1040),
        ],
    )
    ellipse(d, (113, 735, 630, 1185))
    return mask


def mask_royal_salute(size):
    mask = Image.new("L", size, 0)
    d = ImageDraw.Draw(mask)

    poly(d, [(35, 720), (760, 680), (1510, 860), (1515, 1165), (760, 1145), (45, 1110)])
    poly(
        d,
        [
            (1450, 940),
            (1810, 525),
            (2400, 425),
            (3720, 400),
            (3998, 575),
            (3998, 2145),
            (3510, 2180),
            (2440, 2135),
            (1930, 1940),
            (1695, 1685),
            (1515, 1360),
        ],
    )
    ellipse(d, (15, 665, 775, 1150))
    return mask


MASK_BUILDERS = [mask_hibiki, mask_black_bottle, mask_royal_salute]


def soften_alpha(mask):
    # A small contraction removes most background halos, then feathering keeps edges natural.
    contracted = mask.filter(ImageFilter.MinFilter(3))
    return contracted.filter(ImageFilter.GaussianBlur(1.2))


def trim_to_alpha(rgba, padding=40):
    alpha = np.array(rgba.getchannel("A"))
    ys, xs = np.where(alpha > 8)
    if len(xs) == 0 or len(ys) == 0:
        return rgba
    left = max(int(xs.min()) - padding, 0)
    top = max(int(ys.min()) - padding, 0)
    right = min(int(xs.max()) + padding + 1, rgba.width)
    bottom = min(int(ys.max()) + padding + 1, rgba.height)
    return rgba.crop((left, top, right, bottom))


def checkerboard(size, square=40):
    w, h = size
    y, x = np.indices((h, w))
    board = ((x // square + y // square) % 2) * 32 + 205
    return Image.fromarray(np.dstack([board, board, board]).astype(np.uint8), "RGB")


def make_preview(rgba):
    bg = checkerboard(rgba.size)
    bg.paste(rgba, mask=rgba.getchannel("A"))
    return bg


for index, ((src, final_name, preview_name), build_mask) in enumerate(zip(SOURCES, MASK_BUILDERS)):
    image = Image.open(src).convert("RGBA")
    raw_alpha = build_mask(image.size)
    alpha = soften_alpha(raw_alpha)
    image.putalpha(alpha)
    trimmed = trim_to_alpha(image)
    trimmed.save(OUT / final_name)
    make_preview(trimmed).save(OUT / preview_name)
    print(OUT / final_name)
