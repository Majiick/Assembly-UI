/*
    The return structure of the Mnemonic_Redirection_Calculator class.
 */
public class Redirection {
    int address = 0;
    public boolean isRegisterRedirection = false;
    public boolean isMemoryDereference = false;

    Redirection(int address) {
        this.address = address;
    }
    Redirection(int address, boolean isRegisterRedirection) {
        this.address = address;
        this.isRegisterRedirection = isRegisterRedirection;
    }
    Redirection(int address, boolean isRegisterRedirection, boolean isMemoryDereference) {
        this.address = address;
        this.isRegisterRedirection = isRegisterRedirection;
        this.isMemoryDereference = isMemoryDereference;
    }

    public boolean toIAT() {
        if (address > Binary.IAT_RVA && address < Binary.IAT_RVA + Binary.IAT_SIZE) {
            return true;
        }

        return false;
    }
}
