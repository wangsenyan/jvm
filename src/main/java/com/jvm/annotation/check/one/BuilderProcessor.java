package com.jvm.annotation.check.one;


import com.google.auto.service.AutoService;

import javax.tools.Diagnostic;
import javax.tools.Diagnostic.Kind;

import com.jvm.annotation.check.zero.UseCase;
import lombok.SneakyThrows;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ExecutableType;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@SupportedAnnotationTypes("com.jvm.annotation.check.one.BuildProperty")
@SupportedSourceVersion(SourceVersion.RELEASE_14)
@AutoService(Processor.class)
public class BuilderProcessor extends AbstractProcessor {
    @SneakyThrows
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for(TypeElement annotation : annotations){
            Set<? extends Element> annotationElements
                    = roundEnv.getElementsAnnotatedWith(annotation);
            Map<Boolean, List<Element>> annotationMethods = annotationElements.stream().collect(
                    Collectors.partitioningBy(element -> ((ExecutableType) element.asType()).getParameterTypes().size() ==1
                    && element.getSimpleName().toString().startsWith("set")));
            List<Element> setters = annotationMethods.get(true);
            List<Element> otherMethods = annotationMethods.get(false);
            otherMethods.forEach(element ->
                    processingEnv.getMessager().printMessage(Kind.ERROR,
                            "@BuilderProperty must be applied to a setXxx method "
                    + " with a single argument", element));
            if(setters.isEmpty())
                continue;
            String className = ((TypeElement) setters.get(0)
                    .getEnclosingElement()).getQualifiedName().toString();

            Map<String,String> setterMap = setters.stream().collect(Collectors.toMap(
                    setter->setter.getSimpleName().toString(),
                    setter->((ExecutableType)setter.asType())
                    .getParameterTypes().get(0).toString()
            ));
            JavaFileObject builderFile = processingEnv.getFiler()
                    .createSourceFile("PersonBuilder");
        }
        return false;
    }
    private void writeBuilderFile(
            String className, Map<String, String> setterMap)
            throws IOException {

        String packageName = null;
        int lastDot = className.lastIndexOf('.');
        if (lastDot > 0) {
            packageName = className.substring(0, lastDot);
        }

        String simpleClassName = className.substring(lastDot + 1);
        String builderClassName = className + "Builder";
        String builderSimpleClassName = builderClassName
                .substring(lastDot + 1);

        JavaFileObject builderFile = processingEnv.getFiler()
                .createSourceFile(builderClassName);

        try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {

            if (packageName != null) {
                out.print("package ");
                out.print(packageName);
                out.println(";");
                out.println();
            }

            out.print("public class ");
            out.print(builderSimpleClassName);
            out.println(" {");
            out.println();

            out.print("    private ");
            out.print(simpleClassName);
            out.print(" object = new ");
            out.print(simpleClassName);
            out.println("();");
            out.println();

            out.print("    public ");
            out.print(simpleClassName);
            out.println(" build() {");
            out.println("        return object;");
            out.println("    }");
            out.println();

            setterMap.entrySet().forEach(setter -> {
                String methodName = setter.getKey();
                String argumentType = setter.getValue();
                out.print("    public ");
                out.print(builderSimpleClassName);
                out.print(" ");
                out.print(methodName);
                out.print("(");
                out.print(argumentType);
                out.println(" value) {");
                out.print("        object.");
                out.print(methodName);
                out.println("(value);");
                out.println("        return this;");
                out.println("    }");
                out.println();
            });

            out.println("}");
        }
    }
}
