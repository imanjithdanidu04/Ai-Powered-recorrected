#!/usr/bin/env python3
"""Verify that the 13 command prompts in the Kotlin data model are byte-for-byte
identical to the canonical build-spec table.

Usage:
    python3 scripts/verify_prompts.py [path/to/build-spec.md]

Default spec path: <repo-parent>/build-spec.md  (i.e. /home/team/shared/build-spec.md
in the shared workspace). Exits 0 on success, 1 on any mismatch.
"""
import re
import sys
from pathlib import Path

SPEC_DEFAULT = Path(__file__).resolve().parent.parent.parent / "build-spec.md"
KOTLIN_MODEL = (
    Path(__file__).resolve().parent.parent
    / "app/src/main/java/com/textflow/app/data/TextFlowCommand.kt"
)

TRIGGER_ORDER = [
    "typi", "fix", "summ", "polite", "casual", "expand", "translate",
    "bullet", "improve", "rephrase", "emoji", "formal", "funny",
]


def parse_spec(spec_path: Path) -> list[dict]:
    rows = []
    for line in spec_path.read_text(encoding="utf-8").splitlines():
        m = re.match(r"\|\s*`(@[a-z]+)`\s*\|\s*([^|]+?)\s*\|\s*(.*?)\s*\|", line)
        if m:
            rows.append({"trigger": m.group(1), "label": m.group(2), "prompt": m.group(3)})
    return rows


def parse_kotlin(kotlin_path: Path) -> list[dict]:
    text = kotlin_path.read_text(encoding="utf-8")
    blocks = re.findall(
        r"TextFlowCommand\(\s*trigger\s*=\s*\"([a-z]+)\"[^)]*?label\s*=\s*\"([^\"]*)\"[^)]*?systemPrompt\s*=\s*\"([^\"]*)\"\s*,\s*\)",
        text,
        flags=re.S,
    )
    return [{"trigger": t, "label": l, "prompt": p} for t, l, p in blocks]


def main() -> int:
    spec_path = Path(sys.argv[1]) if len(sys.argv) > 1 else SPEC_DEFAULT
    spec_rows = parse_spec(spec_path)
    kotlin_rows = parse_kotlin(KOTLIN_MODEL)

    errors = []
    if len(spec_rows) != 13:
        errors.append(f"spec has {len(spec_rows)} command rows (expected 13)")
    if len(kotlin_rows) != 13:
        errors.append(f"kotlin model has {len(kotlin_rows)} commands (expected 13)")

    spec_by_trigger = {r["trigger"]: r for r in spec_rows}
    for idx, trigger in enumerate(TRIGGER_ORDER):
        spec = spec_by_trigger.get("@" + trigger)
        if spec is None:
            errors.append(f"missing @{trigger} in spec")
            continue
        if idx >= len(kotlin_rows):
            errors.append(f"missing @{trigger} in kotlin model")
            continue
        k = kotlin_rows[idx]
        if k["trigger"] != trigger:
            errors.append(f"order mismatch at #{idx}: kotlin={k['trigger']} spec-order={trigger}")
        if k["label"] != spec["label"]:
            errors.append(f"@{trigger}: label mismatch\n  spec:   {spec['label']!r}\n  kotlin: {k['label']!r}")
        if k["prompt"] != spec["prompt"]:
            errors.append(f"@{trigger}: PROMPT MISMATCH\n  spec:   {spec['prompt']!r}\n  kotlin: {k['prompt']!r}")

    if errors:
        print("VERIFY FAILED")
        for e in errors:
            print(" -", e)
        return 1
    print(f"VERIFY OK: {len(kotlin_rows)}/13 prompts byte-for-byte identical to spec table")
    return 0


if __name__ == "__main__":
    sys.exit(main())
