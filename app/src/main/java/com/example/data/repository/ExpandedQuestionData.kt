package com.example.data.repository

import com.example.data.local.BankQuestionEntity
import com.example.data.model.Grade
import com.example.data.model.Question
import com.example.data.model.Subject

object ExpandedQuestionData {
    val extraCuratedQuestions: List<Question> = listOf(
        // === 4年级 新增 ===
        Question(
            id = "g4_cn_4",
            grade = Grade.GRADE_4,
            subject = Subject.CHINESE_ESSAY,
            title = "【作文选材与情感】写《记一件感动的事》，下列哪种题材最容易写出真情实感、避免虚假套路？",
            options = listOf(
                "下雨天我突然把伞借给陌生人，自己淋雨发高烧，受到大家表扬。",
                "妈妈在昏暗台灯下一针一线帮我缝补校服破洞，眼神里的温柔与关怀。",
                "我在路上捡到一个装有10万元巨款的钱包，毫不犹豫交给警察叔叔。",
                "我帮助老爷爷推了五个小时的三轮车爬大坡。"
            ),
            correctIndex = 1,
            explanation = "小学作文最忌讳“假大空”的拔高套作。选项B抓住了家庭日常中的微小细节——昏暗台灯、缝补校服、温柔眼神，细致真实，以小见大，最能打动人心。其他选项均为编造痕迹明显的套路作文。",
            keyPoints = listOf("作文情感真实原则：真人真事真感情", "避免流水账和失真的虚假宏大题材"),
            sampleEssence = "佳作金句：“母爱往往没有惊天动地的誓言，只融化在昏黄台灯下，那一下又一下无声穿梭的细线里。”"
        ),
        Question(
            id = "g4_ma_3",
            grade = Grade.GRADE_4,
            subject = Subject.MATH,
            title = "【植树问题与间隔】在一条长 100 米的小路一侧植树，每隔 5 米栽一棵（两端都要栽）。一共需要栽多少棵树？",
            options = listOf(
                "20 棵",
                "21 棵",
                "19 棵",
                "22 棵"
            ),
            correctIndex = 1,
            explanation = "两端都栽的植树模型公式：棵数 = 间隔数 + 1。间隔数 = 全长 ÷ 间距 = 100 ÷ 5 = 20 个间隔。因此棵数 = 20 + 1 = 21 棵。",
            keyPoints = listOf("两端都栽：棵数 = 间隔数 + 1", "只栽一端：棵数 = 间隔数", "两端都不栽：棵数 = 间隔数 - 1"),
            sampleEssence = "速记口诀：两端都栽加一树，两端不栽减一树，封闭图形间隔等于树！"
        ),
        Question(
            id = "g4_en_3",
            grade = Grade.GRADE_4,
            subject = Subject.ENGLISH,
            title = "【名词单复数与可数不可数】There ______ some bread and two ______ on the dining table.",
            options = listOf(
                "is; apples",
                "are; apples",
                "is; apple",
                "are; bread"
            ),
            correctIndex = 0,
            explanation = "There be 句型遵循“就近原则”。距离 be 动词最近的主语是不可数名词 bread，因此 be 动词用 is；后面由 two 修饰可数名词 apple，应用复数 apples，故选 is; apples。",
            keyPoints = listOf("There be 句型就近原则：靠近 be 动词的主语决定单复数", "bread 是不可数名词，不能直接加 s"),
            sampleEssence = "必考口诀：There be 句型有特点，主语单复看在前；就近原则牢牢记，单数不可数都用 is。"
        ),

        // === 5年级 新增 ===
        Question(
            id = "g5_cn_3",
            grade = Grade.GRADE_5,
            subject = Subject.CHINESE_ESSAY,
            title = "【叙事节奏与波澜】作文怎样才能做到“波澜起伏、引人入胜”？",
            options = listOf(
                "把所有事情从头到尾按时间流水账详细记录，不落下一分钟。",
                "故意编造奇形怪状的科幻外星人打架故事。",
                "巧设悬念、运用欲扬先抑、安排合理的小误会或曲折转折。",
                "全文大量使用感叹号和夸张词汇营造紧张气氛。"
            ),
            correctIndex = 2,
            explanation = "古人云：“文似看山不喜平”。写记叙文时巧设悬念（开头提问或设疑）、欲扬先抑（先抑后扬）或安排巧妙的情节转折，能让故事一波三折，产生强烈的阅读吸引力。",
            keyPoints = listOf("波澜技法：悬念铺垫、欲扬先抑、一波三折", "文似看山不喜平：避免平铺直叙"),
            sampleEssence = "作文秘籍：“好文如登山，起伏现峰峦；先抑而后扬，笔底见波澜。”"
        ),
        Question(
            id = "g5_ma_3",
            grade = Grade.GRADE_5,
            subject = Subject.MATH,
            title = "【长方体表面积与体积】一个长方体的长、宽、高分别扩大到原来的 2 倍，那么它的体积会扩大到原来的多少倍？",
            options = listOf(
                "2 倍",
                "4 倍",
                "6 倍",
                "8 倍"
            ),
            correctIndex = 3,
            explanation = "长方体体积公式为 V = 长 × 宽 × 高。三个维度各扩大到原来的 2 倍，新体积 = (长×2) × (宽×2) × (高×2) = 原体积 × (2 × 2 × 2) = 原体积 × 8。所以体积扩大到原来的 8 倍（面积扩大到4倍）。",
            keyPoints = listOf("立体图形各边长扩大 n 倍，表面积扩大 n² 倍，体积扩大 n³ 倍", "2³ = 8"),
            sampleEssence = "考点倍数规律：长度扩大 n 倍，面积看平方(n²)，体积看立方(n³)。"
        ),
        Question(
            id = "g5_en_3",
            grade = Grade.GRADE_5,
            subject = Subject.ENGLISH,
            title = "【频度副词与习惯】My father ______ goes to bed late because he thinks staying up late is bad for health.",
            options = listOf(
                "always",
                "seldom",
                "usually",
                "often"
            ),
            correctIndex = 1,
            explanation = "后半句解释原因“because he thinks staying up late is bad for health（因为他认为熬夜有害健康）”，因此爸爸应该是“很少/几乎不”很晚睡觉。seldom 意为“很少、不常”，符合语境。always(总是)、usually(通常)、often(经常)均与因果语义相反。",
            keyPoints = listOf("频度副词辨析：always (100%) > usually > often > sometimes > seldom (很少) > never (从不)", "语境逻辑因果分析"),
            sampleEssence = "核心词记忆：seldom = rarely = not often (很少，几乎不)。"
        ),

        // === 6年级 新增 ===
        Question(
            id = "g6_cn_3",
            grade = Grade.GRADE_6,
            subject = Subject.CHINESE_ESSAY,
            title = "【排比修辞与升华】在毕业典礼作文的结尾，使用排比句的主要表达效果是？",
            options = listOf(
                "增加字数凑够作文篇幅要求。",
                "节奏鲜明，增强语势，淋漓尽致地抒发感恩与不舍的浓厚情感。",
                "让文章显得非常深奥难懂，显示文采。",
                "代替文章事实内容的描写。"
            ),
            correctIndex = 1,
            explanation = "排比修辞（三个或三个以上结构相同或相似、语气一致、意思密切相关的句子排列）在文章结尾具有极强的抒情与气势渲染效果，读起来朗朗上口，把情感推向最高潮。",
            keyPoints = listOf("排比修辞作用：句式整齐、节奏鲜明、增强语势、抒情强烈", "抒情散文结尾压轴必备技巧"),
            sampleEssence = "排比升华示范：“母校是清晨迎着朝阳的书声，是操场洒下汗水的跑道，更是一生中温暖心灵的精神灯塔。”"
        ),
        Question(
            id = "g6_ma_3",
            grade = Grade.GRADE_6,
            subject = Subject.MATH,
            title = "【百分数与利润折扣】一件衣服标价 200 元，商场搞活动先涨价 20%，随后又打八折（降价20%）出售。现价是多少元？",
            options = listOf(
                "200 元",
                "192 元",
                "208 元",
                "180 元"
            ),
            correctIndex = 1,
            explanation = "单位“1”发生了变化！第一次涨价后：200 × (1 + 20%) = 240 元；第二次降价是在 240 元的基础上降价 20%：240 × (1 - 20%) = 240 × 0.8 = 192 元。并非不变，而是比原价少了 8 元！",
            keyPoints = listOf("抓住两次变化的不同单位‘1’", "连续涨降同等百分比，最终价格必然低于原价"),
            sampleEssence = "商业陷阱必知：同幅度先涨后降或先降后涨，最终必亏（(1+x)(1-x) = 1-x² < 1）。"
        ),
        Question(
            id = "g6_en_3",
            grade = Grade.GRADE_6,
            subject = Subject.ENGLISH,
            title = "【情景交际与综合】— Would you like some more juice? — ______, I'm full.",
            options = listOf(
                "Yes, please",
                "No, thanks",
                "Sure, of course",
                "No, I don't"
            ),
            correctIndex = 1,
            explanation = "对于他人提供食物/饮品的礼貌提议 'Would you like...?'，接受时回答 'Yes, please'，委婉礼貌拒绝时必须使用 'No, thanks'（不用了，谢谢）。",
            keyPoints = listOf("礼貌提出提议：Would you like...?", "委婉礼貌拒绝标配回答：No, thanks / No, thank you"),
            sampleEssence = "小学口语交际高频礼貌搭配：Would you like...? -> Yes, please / No, thanks."
        )
    )

    fun Question.toEntity(isRemote: Boolean = false): BankQuestionEntity {
        return BankQuestionEntity(
            id = this.id,
            gradeLevel = this.grade.level,
            subjectType = this.subject.name,
            title = this.title,
            optionsJson = this.options.joinToString("|||"),
            correctIndex = this.correctIndex,
            explanation = this.explanation,
            sampleEssence = this.sampleEssence,
            keyPointsJson = this.keyPoints.joinToString("|||"),
            isCustomOrRemote = isRemote
        )
    }

    fun BankQuestionEntity.toModel(): Question {
        val gradeObj = Grade.values().find { it.level == this.gradeLevel } ?: Grade.GRADE_4
        val subjObj = runCatching { Subject.valueOf(this.subjectType) }.getOrDefault(Subject.CHINESE_ESSAY)
        val opts = this.optionsJson.split("|||")
        val kps = if (this.keyPointsJson.isNotBlank()) this.keyPointsJson.split("|||") else emptyList()
        return Question(
            id = this.id,
            grade = gradeObj,
            subject = subjObj,
            title = this.title,
            options = opts,
            correctIndex = this.correctIndex,
            explanation = this.explanation,
            sampleEssence = this.sampleEssence,
            keyPoints = kps
        )
    }
}
