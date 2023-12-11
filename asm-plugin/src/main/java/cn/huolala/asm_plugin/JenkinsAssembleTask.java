package cn.huolala.asm_plugin;

import org.gradle.api.Project;
import org.gradle.api.tasks.Exec;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.inject.Inject;


public class JenkinsAssembleTask extends Exec {
    private final Project mProject;

    /**
     * 除了在Task创建后配置参数，我们也可以将参数传递给Task的构建函数，为了实现这点，我们必须使用@Inject注解
     *
     * @param project project
     */
    @Inject
    public JenkinsAssembleTask(Project project) {
        mProject = project;
    }

    // @TaskAction注解，指明当前Task执行的方法
    @TaskAction
    void doJenkins() {
        if (mProject == null) {
            return;
        }
        //预设一些参数
        System.out.println("invoke JenkinsAssembleTask method doJenkins");
        System.out.println("project " + mProject.getName());
        String PROPERTY_NAME = "buildConfig";
        String REPLACE_HOLDER = ",";
        String PARAM_COMMAND = " -P ";
        if (mProject.hasProperty(PROPERTY_NAME)) {
            String buildConfig = (String) mProject.findProperty(PROPERTY_NAME);
            File workingDir = mProject.getRootDir();
            System.out.println("start jeakins build apk process, config : " + buildConfig + "\nworkingDir: " + workingDir);
            if (buildConfig != null) {
                buildConfig = buildConfig.replaceAll(REPLACE_HOLDER, System.lineSeparator());
                // parse config string to Properties
                Properties props = new Properties();
                try {
                    props.load(new StringReader(buildConfig));
                    // start to assemble shell commend
                    List<String> commands = getBaseCommand();
                    StringBuilder builder = new StringBuilder();
                    if (isWindowsEnvironment()) {
                        builder.append(" gradlew ");
                    } else {
                        builder.append("bash ./gradlew ");
                    }
                    // convert to real gradle task
                    builder.append(combineRealGradleTask(props));
                    builder.append(" --stacktrace -info ");

                    // Transfer new parameters
                    builder.append(PARAM_COMMAND).append("appAssembleConfig=");
                    if (isWindowsEnvironment()) {
                        builder.append("\"${realBuildConfig}\"");
                    } else {
                        builder.append("${realBuildConfig}");
                    }
                    commands.add(builder.toString());
                    System.out.println("execute comand is : " + builder);
                    setCommandLine(commands);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            System.out.println("not in jeakins build apk process ");
        }
    }

    private List<String> getBaseCommand() {
        List<String> commands = new ArrayList<>();
        if (isWindowsEnvironment()) {
            commands.add("cmd");
            commands.add("/c");
        } else {
            commands.add("bash");
            commands.add("-c");
        }
        return commands;
    }

    private boolean isWindowsEnvironment() {
        return System.getProperty("os.name").toLowerCase().startsWith("windows");
    }

    // 各自APP可以面对不同的情况，可以做不同的修改，组合执行特殊的task
    private String combineRealGradleTask(Properties props) {
        StringBuilder builder = new StringBuilder();
        String subject = props.getProperty("subProject", "app");
        builder.append(":").append(subject).append(":assemble");
        String  brandType = getBrandType(props);
        builder.append(brandType);
        boolean isRelease = Boolean.parseBoolean(props.getProperty("isRelease", "true")); // 默认编译release
        if (isRelease) {
            builder.append("Release");
        } else {
            builder.append("Debug");
        }
        return builder.toString();
    }

    private String getBrandType(Properties props) {
        String brandType = props.getProperty("CONFIG_VEHICLE_MODLE", "");  // 默认全量
        if (mProject.hasProperty("brandTypeMap") && mProject.findProperty("brandTypeMap") instanceof Map) {
            Object object = mProject.findProperty("brandTypeMap");
            if (object instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) object;
                String brand = (String) map.get(brandType);
                if (brand != null && !brand.isEmpty()) {
                    brandType = brand;
                } else {
                    brandType = "";
                }
            }
        } else {
            System.out.println("has not set brand map ");
        }
        return brandType;
    }
}
