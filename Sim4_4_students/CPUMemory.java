public class CPUMemory {
    public int[] instMemory;
    public int[] dataMemory;
    public int pc;
    public int[] regs;

    public CPUMemory() {
        instMemory = new int[1024];
        dataMemory = new int[16*1024];
        regs = new int[34];  // 32 registers + lo/hi
    }
}