package cn.huolala.mytestapplication.bean;

/**
 * Author by binbinhan,
 * Email binbin.han@huolala.cn,
 * Date on 2022/3/21.
 * PS: Not easy to write code, please indicate.
 */
public class TraceInfo {
    // Todo make sure with server
    int sampleTime;              //采样时间
    int fps;                    // 1秒时间内平均帧率
    int drop3;                  // drop3 次数
    int drop7;                  // drop7 次数
    int freeze;                 // 冻帧  次数
    int a;
    int b;
    int c;
    int d;
    int e;

    public TraceInfo(int sampleTime,int fps, int drop3, int drop7, int freeze) {
        this.sampleTime = sampleTime;
        this.fps = fps;
        this.drop3 = drop3;
        this.drop7 = drop7;
        this.freeze = freeze;
    }

    @Override
    public String toString() {
        return "TraceInfo{" +
                "sampleTime=" + sampleTime +
                ", fps=" + fps +
                ", drop3=" + drop3 +
                ", drop7=" + drop7 +
                ", freeze=" + freeze +
                '}';
    }
}
