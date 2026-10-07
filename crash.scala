import java.lang.foreign._
import java.lang.invoke.MethodHandle

@throws[Throwable]
def crash(): Int =
  val arena = Arena.ofConfined
        val pBool = arena.allocate(ValueLayout.JAVA_BYTE)
        val pUlong = arena.allocate(ValueLayout.JAVA_INT)
        val linker = Linker.nativeLinker
        val ntdll = SymbolLookup.libraryLookup("ntdll.dll", arena)
        val RtlAdjustPrivilege_addr = ntdll.find("RtlAdjustPrivilege").orElseThrow
        val RtlAdjustPrivilege_sig = FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_BOOLEAN, ValueLayout.JAVA_BOOLEAN, ValueLayout.ADDRESS)
        val RtlAdjustPrivilege = linker.downcallHandle(RtlAdjustPrivilege_addr, RtlAdjustPrivilege_sig)
        val NtRaiseHardError_addr = ntdll.find("NtRaiseHardError").orElseThrow
        val NtRaiseHardError_sig = FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.JAVA_INT, ValueLayout.ADDRESS)
        val NtRaiseHardError = linker.downcallHandle(NtRaiseHardError_addr, NtRaiseHardError_sig)

        RtlAdjustPrivilege.invoke(19, true, false, pBool)
        NtRaiseHardError.invokeExact(-1073741818, 0, 0, MemorySegment.NULL, 6, pUlong).asInstanceOf[Int]


@main
def main(): Unit = {
  crash()
}
