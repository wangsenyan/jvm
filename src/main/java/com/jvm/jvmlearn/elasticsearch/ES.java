package com.jvm.jvmlearn.elasticsearch;

import java.lang.module.ModuleDescriptor;

public class ES {
    public static void main(String[] args) {
        try{
            String inputValues[] = {"cat", "deep", "do", "dog", "dogs"};
                      long outputValues[] = {5, 7, 17, 18, 21};
//                        PositiveIntOutputs outputs = PositiveIntOutputs.getSingleton(true);
//                        Builder<Long> builder = new ModuleDescriptor.Builder<Long>(FST.INPUT_TYPE.BYTE1, outputs);
//                        BytesRef scratchBytes = new BytesRef();
//                         IntsRef scratchInts = new IntsRef();
//                        for (int i = 0; i < inputValues.length; i++) {
//                                 scratchBytes.copyChars(inputValues[i]);
//                                 builder.add(Util.toIntsRef(scratchBytes, scratchInts), outputValues[i]);
//                             }
//                         FST<Long> fst = builder.finish();
//                         Long value = Util.get(fst, new BytesRef("dog"));
//                         System.out.println(value); // 18
        }catch (Exception e){
            ;
        }
    }
}
