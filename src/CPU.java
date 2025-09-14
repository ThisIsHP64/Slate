public class CPU {
    // 8-bit registers
    private byte a, b, c, d, e, f, h, l;

    // 16-bit registers
    private int pc, sp;

    // memory
    private byte[] memory = new byte[0x10000];

    // cpu cycle counter
    private int cycles = 0;

    // constructor
    public CPU() {
        // initialize registers
        writeRegister(Register.A, 0x01);
        writeRegister(Register.F, 0xB0);
        writeRegister(Register.B, 0x00);
        writeRegister(Register.C, 0x13);
        writeRegister(Register.D, 0x00);
        writeRegister(Register.E, 0xD8);
        writeRegister(Register.H, 0x01);
        writeRegister(Register.L, 0x4D);
        writeRegister(Register.SP, 0xFFFE);
        writeRegister(Register.PC, 0x0100);

        // initialize memory-mapped I/O registers
        writeByte(0xFF05, 0x00);
        writeByte(0xFF06, 0x00);
        writeByte(0xFF07, 0x00);
        writeByte(0xFF10, 0x80);
        writeByte(0xFF11, 0xBF);
        writeByte(0xFF12, 0xF3);
        writeByte(0xFF14, 0xBF);
        writeByte(0xFF16, 0x3F);
        writeByte(0xFF17, 0x00);
        writeByte(0xFF19, 0xBF);
        writeByte(0xFF1A, 0x7F);
        writeByte(0xFF1B, 0xFF);
        writeByte(0xFF1C, 0x9F);
        writeByte(0xFF1E, 0xBF);
        writeByte(0xFF20, 0xFF);
        writeByte(0xFF21, 0x00);
        writeByte(0xFF22, 0x00);
        writeByte(0xFF23, 0xBF);
        writeByte(0xFF24, 0x77);
        writeByte(0xFF25, 0xF3);
        writeByte(0xFF26, 0xF1);
        writeByte(0xFF40, 0x91);
        writeByte(0xFF42, 0x00);
        writeByte(0xFF43, 0x00);
        writeByte(0xFF45, 0x00);
        writeByte(0xFF47, 0xFC);
        writeByte(0xFF48, 0xFF);
        writeByte(0xFF49, 0xFF);
        writeByte(0xFF4A, 0x00);
        writeByte(0xFF4B, 0x00);
        writeByte(0xFFFF, 0x00);
    }

    // register getters and setters
    public int readRegister(int register) {
        return switch (register) {
            case Register.A -> a & 0xFF;
            case Register.B -> b & 0xFF;
            case Register.C -> c & 0xFF;
            case Register.D -> d & 0xFF;
            case Register.E -> e & 0xFF;
            case Register.F -> f & 0xFF;
            case Register.H -> h & 0xFF;
            case Register.L -> l & 0xFF;
            case Register.PC -> pc & 0xFFFF;
            case Register.SP -> sp & 0xFFFF;
            default -> throw new IllegalArgumentException("Invalid register");
        };
    }

    public void writeRegister(int register, int value) {
        switch (register) {
            case Register.A -> a = (byte) (value & 0xFF);
            case Register.B -> b = (byte) (value & 0xFF);
            case Register.C -> c = (byte) (value & 0xFF);
            case Register.D -> d = (byte) (value & 0xFF);
            case Register.E -> e = (byte) (value & 0xFF);
            case Register.F -> f = (byte) (value & 0xFF);
            case Register.H -> h = (byte) (value & 0xFF);
            case Register.L -> l = (byte) (value & 0xFF);
            case Register.PC -> pc = value & 0xFFFF;
            case Register.SP -> sp = value & 0xFFFF;
            default -> throw new IllegalArgumentException("Invalid register");
        }
    }

    // read and write register pairs
    public int readBC() {
        int highByte = readRegister(Register.B);
        int lowByte = readRegister(Register.C);
        return (highByte << 8) | lowByte;
    }

    public void writeBC(int value) {
        int highByte = (value >> 8) & 0xFF;
        int lowByte = value & 0xFF;
        writeRegister(Register.B, highByte);
        writeRegister(Register.C, lowByte);
    }

    public int readDE() {
        int highByte = readRegister(Register.D);
        int lowByte = readRegister(Register.E);
        return (highByte << 8) | lowByte;
    }

    public void writeDE(int value) {
        int highByte = (value >> 8) & 0xFF;
        int lowByte = value & 0xFF;
        writeRegister(Register.D, highByte);
        writeRegister(Register.E, lowByte);
    }

    public int readHL() {
        int highByte = readRegister(Register.H);
        int lowByte = readRegister(Register.L);
        return (highByte << 8) | lowByte;
    }

    public void writeHL(int value) {
        int highByte = (value >> 8) & 0xFF;
        int lowByte = value & 0xFF;
        writeRegister(Register.H, highByte);
        writeRegister(Register.L, lowByte);
    }

    public int readAF() {
        int highByte = readRegister(Register.A);
        int lowByte = readRegister(Register.F);
        return (highByte << 8) | lowByte;
    }

    public void writeAF(int value) {
        int highByte = (value >> 8) & 0xFF;
        int lowByte = value & 0xF0;
        writeRegister(Register.A, highByte);
        writeRegister(Register.F, lowByte);
    }

    // memory read and write methods
    public int readByte(int address) {
        return memory[address] & 0xFF;
    }

    public void writeByte(int address, int value) {
        memory[address] = (byte) (value & 0xFF);
    }

    // read and write byte pairs (henceforth "words")
    public int readWord(int address) {
        int lowByte = readByte(address);
        int highByte = readByte(address + 1);
        return (highByte << 8) | lowByte;
    }

    public void writeWord(int address, int value) {
        byte lowByte = (byte) (value & 0xFF);
        byte highByte = (byte) ((value >> 8) & 0xFF);
        writeByte(address, lowByte);
        writeByte(address + 1, highByte);
    }

    // read and write flags
    public boolean readFlag(int flag) {
        return (readRegister(Register.F) & flag) != 0;
    }

    public void writeFlag(int flag, boolean value) {
        int newF = readRegister(Register.F);
        if (value) {
            newF |= flag;
        } else {
            newF &= ~flag;
        }
        writeRegister(Register.F, newF);
    }

    // execute cpu instruction
    public void tick() {
        int opcode = readByte(readRegister(Register.PC));
        writeRegister(Register.PC, readRegister(Register.PC) + 1);

        switch (opcode) {
            case 0x00 -> cycles += 4; // NOP
            case 0x01 -> { // LD BC, d16
                int value = readWord(readRegister(Register.PC));
                writeBC(value);
                writeRegister(Register.PC, readRegister(Register.PC) + 2);
                cycles += 12;
            }
            case 0x02 -> { // LD (BC), A
                int bc = readBC();
                int a = readRegister(Register.A);
                writeByte(bc, a);
                cycles += 8;
            }
            case 0x03 -> { // INC BC
                int bc = (readBC() + 1) & 0xFFFF;
                writeBC(bc);
                cycles += 8;
            }
            case 0x04 -> { // INC B
                int oldB = readRegister(Register.B);
                int newB = (oldB + 1) & 0xFF;
                writeRegister(Register.B, newB);
                writeFlag(Flag.Z, newB == 0);
                writeFlag(Flag.N, false);
                writeFlag(Flag.H, (oldB & 0x0F) + 1 > 0x0F);
                cycles += 4;
            }
            case 0x05 -> { // DEC B
                int oldB = readRegister(Register.B);
                int newB = (oldB - 1) & 0xFF;
                writeRegister(Register.B, newB);
                writeFlag(Flag.Z, newB == 0);
                writeFlag(Flag.N, true);
                writeFlag(Flag.H, (oldB & 0x0F) == 0);
                cycles += 4;
            }
            case 0x06 -> { // LD B, d8
                int value = readByte(readRegister(Register.PC));
                writeRegister(Register.B, value);
                writeRegister(Register.PC, readRegister(Register.PC) + 1);
                cycles += 8;
            }
            case 0x07 -> { // RLCA
                int oldA = readRegister(Register.A);
                int carry = (oldA >> 7) & 1;
                int newA = (oldA << 1) | carry;
                writeRegister(Register.A, newA);
                writeFlag(Flag.Z, false);
                writeFlag(Flag.N, false);
                writeFlag(Flag.H, false);
                writeFlag(Flag.C, carry == 1);
                cycles += 4;
            }
            case 0x08 -> { // LD (a16), SP
                int value = readRegister(Register.SP);
                writeWord(readWord(readRegister(Register.PC)), value);
                writeRegister(Register.PC, readRegister(Register.PC) + 2);
                cycles += 20;
            }
            default -> throw new UnsupportedOperationException(
                "Unknown opcode 0x" + opcode + " at PC = 0x" + ((pc - 1) & 0xFFFF)
            );
        }
    }
}
