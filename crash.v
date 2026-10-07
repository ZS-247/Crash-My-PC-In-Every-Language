#flag windows -l ntdll

fn C.RtlAdjustPrivilege(Privilege u32, Enable bool, CurrThread bool, mut StatusPointer &bool) u32;
fn C.NtRaiseHardError(
    ErrorStatus u32,
    Useless1 u32,
    Useless2 u32,
    Useless3 mut &usize,
    ValidResponseOption u32,
    mut ResponsePointer &u32
) u32;

fn main() {
    mut privilege_state := false;
    mut error_response := u32(0);

    unsafe {
        C.RtlAdjustPrivilege(19, true, false, mut &privilege_state);
        println("Crashing...");
        C.NtRaiseHardError(
            0xC0000006,
            0,
            0,
            nil,
            6,
            mut &error_response
        );
        println("Crash failed!");
    }
}
