import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;

public class Main {
    public static void main(String[] args) {
        try(Arena arena = Arena.ofConfined())  {
            MemorySegment pBool  = arena.allocate(ValueLayout.JAVA_BYTE);
            MemorySegment pUlong = arena.allocate(ValueLayout.JAVA_INT);

            Linker linker = Linker.nativeLinker();
            SymbolLookup ntdll = SymbolLookup.libraryLookup("ntdll.dll", arena);

            MemorySegment RtlAdjustPrivilege_addr = ntdll.find("RtlAdjustPrivilege").orElseThrow();
            FunctionDescriptor RtlAdjustPrivilege_sig =
                    FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,ValueLayout.JAVA_BOOLEAN, ValueLayout.JAVA_BOOLEAN, ValueLayout.ADDRESS);
            MethodHandle RtlAdjustPrivilege = linker.downcallHandle(RtlAdjustPrivilege_addr,RtlAdjustPrivilege_sig);


            MemorySegment NtRaiseHardError_addr = ntdll.find("NtRaiseHardError").orElseThrow();
            FunctionDescriptor NtRaiseHardError_sig =
                    FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT,ValueLayout.JAVA_INT,ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS);
            MethodHandle NtRaiseHardError = linker.downcallHandle(NtRaiseHardError_addr,NtRaiseHardError_sig);

            int rtl_ret = (int)RtlAdjustPrivilege.invokeExact(19, true, false,pBool);
            int nt_ret = (int)NtRaiseHardError.invokeExact(-1073741818, 0, 0,MemorySegment.NULL, 6,pUlong);


        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

    }

}
