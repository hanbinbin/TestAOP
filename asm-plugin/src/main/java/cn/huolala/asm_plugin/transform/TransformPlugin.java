package cn.huolala.asm_plugin.transform;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

import cn.huolala.asm_plugin.JenkinsAssembleTask;

/**
 * Author by binbinhan,
 * Email binbin.han@huolala.cn,
 * Date on 2/27/22.
 * PS: Not easy to write code, please indicate.
 */
public class TransformPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
//        project.getExtensions().getByType(AppExtension.class).registerTransform(new ASMTransform());
        //添加自定义任务
        project.getTasks().create("assembleJenkinsBuild", JenkinsAssembleTask.class, project);
    }
}
