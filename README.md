# Slate
<img width="200" height="200" alt="logo" src="https://github.com/user-attachments/assets/9b48bf27-5887-4461-9461-2d04b0013d90" />

A work-in-progress emulator for the Nintendo Game Boy (DMG-01) written in Java.

## But why?
I wanted to take a journey outside of my comfort zone (high-level Java programming and mobile app development in Kotlin) and explore such things as low-level programming, how CPUs actually work, and reasonably complex graphics. I am also an enthusiast of legacy hardware, particularly Nintendo gaming hardware, so looking at the internal workings of the Game Boy is quite interesting to me. I know [JavaBoy](https://github.com/DidgeridooMH/JavaBoy) exists, and does much more than I intend for this emulator to do even in its finished state, but I am building Slate as a passion project to learn about the basics of Assembly, improve my graphics skills, and simply for the fun of it.

## How long do you expect Slate to take to complete?
Several years. I am working on Slate in my free time, particularly when I am on break from university. Expect bursts of progress over the summer and winter months, with little to no progress during the autumn and spring.

## Isn't there a better way to do (x)?
Probably. My initial goal is functionality, even if some implementations will be naive. Optimizations will come later.

## What do you want to implement?
In order:
- 1:1 replica of the Sharp LR35902 written in Java
- Ability to play .gb/.gbc-formatted Game Boy cartridge dumps
- 4-shade monochrome 160x144 graphics
- On-screen and keyboard controls
- 1:1 replica of the APU

...and not much more. Slate is intended to be a bare-bones emulator built for learning purposes.

## Current progress?
Right now, Slate is a CPU scaffold with a few opcodes implemented. No support for playing games, audio, or graphics yet - that will come later.

## Will you accept my pull request?
No. I intend to develop Slate completely independently.

## What devices will be able to run Slate?
Anything that can run modern Java (basically every modern PC/Mac can do this).

## Thank you for your support!

I know Slate doesn't look like much right now, but I will see it through, no matter how long that takes.

Feel free to contact me on Discord @thisishp64 if you have any questions.
