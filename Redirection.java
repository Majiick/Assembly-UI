/*
    The return structure of the Mnemonic_Redirection_Calculator class.
 */
public class Redirection {
    int address = 0;
    public boolean isRegisterRedirection = false;

    Redirection(int address) {
        this.address = address;
    }
    Redirection(int address, boolean isRegisterRedirection) {
        this.address = address;
        this.isRegisterRedirection = isRegisterRedirection;
    }

    public boolean toIAT() {
        if (address > Test.IAT_RVA && address < Test.IAT_RVA + Test.IAT_SIZE) {
            return true;
        }

        return false;
    }
}
