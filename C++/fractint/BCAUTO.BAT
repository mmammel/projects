@attrib -r fractint.exe
@make -fbcfract.mak fractint.exe > bcfract.log
@attrib +r fractint.exe
type bcfract.log | more
