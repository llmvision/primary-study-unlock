package com.example.data.repository

import com.example.data.model.Grade
import com.example.data.model.Question
import com.example.data.model.Subject

/**
 * ============================================================================
 * 小学 4-6 年级知识题库研发生成架构 (Question Bank Generation Architecture)
 * ============================================================================
 *
 * 1. 题库设计哲学与目标：
 *    - 针对小学 4-6 年级高频易错与新课标考点定制，旨在通过碎片化锁屏通关激发兴趣；
 *    - 重点突破语文作文（修辞、拟人、开头先声夺人、细节刻画、以小见大立意、文言文哲理）；
 *    - 夯实数学核心思维（简便运算拆分、几何内角和、因数质数、比例尺、植树模型与立体体积）；
 *    - 强化小学英语高频考点（现在进行时、一般过去时、时间介词速记、形容词比较级变形与常用交际口语）。
 *
 * 2. 命题规范 (Item Generation Protocol)：
 *    - 显性考点标签：每道题干均带有【考点类别】前缀，便于学生形成分类思维；
 *    - 诊断性干扰项 (Diagnostic Distractors)：错误选项精准对应小学生的常见思维盲区；
 *    - 深度双解析：包含通俗易懂的详细名师推导解析，以及专属 sampleEssence（写作金句/速算口诀/语法短语）。
 *
 * 3. 动态扩展与在线更新：
 *    - 配合 BankQuestionDao 与 StudyRepository.syncOnlineQuestionBank()，
 *      支持初始本地冷启动与云端增量无缝更新扩充。
 * ============================================================================
 */
object QuestionBank {
    val allQuestions: List<Question> = listOf(
        // === 4年级 语文/作文 ===
        Question(
            id = "g4_cn_1",
            grade = Grade.GRADE_4,
            subject = Subject.CHINESE_ESSAY,
            title = "【修辞与作文】在描写秋天的景色时，下列哪一句运用了“拟人”的修辞手法，使文章更生动？",
            options = listOf(
                "红红的枫叶像一枚枚邮票，飘哇飘哇。",
                "黄黄的银杏叶在秋风中跳着欢快的舞蹈，向大地问好。",
                "田野像金色的海洋，一眼望不到边。",
                "秋天的果实多得像天上的星星。"
            ),
            correctIndex = 1,
            explanation = "选项B中银杏叶“跳着欢快的舞蹈，向大地问好”，把银杏叶当成人来写，赋予其动作与情感，属于典型的“拟人”手法。选项A、C、D均带有比喻词“像”，属于比喻修辞。",
            keyPoints = listOf("拟人修辞手法特征：赋予事物人的动作、言语或神态", "写景作文妙招：用拟人让草木山石‘活’起来"),
            sampleEssence = "佳句积累：“秋风轻轻哼唱着小调，顽皮的落叶在林间打着旋儿跳舞。”"
        ),
        Question(
            id = "g4_cn_2",
            grade = Grade.GRADE_4,
            subject = Subject.CHINESE_ESSAY,
            title = "【作文开头技巧】写一篇《记一次难忘的春游》的记叙文，下列哪个开头最有吸引力？",
            options = listOf(
                "今天天气晴朗，万里无云，我们学校组织去春游。",
                "“哇！太美啦！”伴随着阵阵欢呼，我们的大巴车缓缓驶进了生机盎然的翠华山。",
                "春游是一件很有意义的事，很多人都喜欢春游。",
                "早上七点我起床刷牙洗脸，然后去学校集合准备春游。"
            ),
            correctIndex = 1,
            explanation = "好的作文开头讲究“先声夺人”。选项B采用“声音/情境导入法”，一开篇就通过欢呼声带出热闹期待的场景，迅速抓住读者眼球。而A和D是流水账式平铺直叙，C偏向空泛议论。",
            keyPoints = listOf("作文开头技巧：先声夺人、悬念引入或精彩特写", "避免记流水账式的作息陈述"),
            sampleEssence = "开头金句：未见其景，先闻其声。善用感叹句和现场细节点燃读者好奇心。"
        ),
        Question(
            id = "g4_cn_3",
            grade = Grade.GRADE_4,
            subject = Subject.CHINESE_ESSAY,
            title = "【古诗文积累】“不识庐山真面目，只缘身在此山中。”这两句诗出自苏轼的《题西林壁》，它蕴含的哲理是？",
            options = listOf(
                "庐山的风景常年被大雾遮挡，谁也看不清楚。",
                "要想看清事物全貌，必须身临其境仔细观察。",
                "由于受主观位置或局限影响，身在其中往往难以客观看清事物的整体真相。",
                "爬山越深入，危险就越大。"
            ),
            correctIndex = 2,
            explanation = "苏轼借游庐山的体会说明哲理：身在庐山深处，眼界被峰峦所障，因此看不清庐山全貌。比喻由于受主观认识或身处局部的限制，容易片面，需要跳出局部看全局。",
            keyPoints = listOf("《题西林壁》：苏轼所作哲理名诗", "哲理：当局者迷，旁观者清；跳出局部看全局"),
            sampleEssence = "作文点睛哲理：在议论文或反思作文结尾，可引用此诗句升华立意。"
        ),

        // === 4年级 数学 ===
        Question(
            id = "g4_ma_1",
            grade = Grade.GRADE_4,
            subject = Subject.MATH,
            title = "【运算律与简便计算】计算 125 × 32 × 25，最简便合理的拆分方法是？",
            options = listOf(
                "(125 × 30) + (125 × 2) × 25",
                "(125 × 8) × (4 × 25)",
                "(125 × 25) × 32",
                "125 × (32 + 25)"
            ),
            correctIndex = 1,
            explanation = "我们要牢记两个好朋友数对：125 × 8 = 1000，以及 25 × 4 = 100。把 32 拆分成 8 × 4，原式变为 (125 × 8) × (4 × 25) = 1000 × 100 = 100,000，口算即可完成！",
            keyPoints = listOf("乘法结合律与分配律巧妙应用", "核心速算数字对：125×8=1000，25×4=100"),
            sampleEssence = "简算口诀：见125想8凑整千，见25想4凑整百，乘法结合变轻松！"
        ),
        Question(
            id = "g4_ma_2",
            grade = Grade.GRADE_4,
            subject = Subject.MATH,
            title = "【几何图形】在一个三角形中，已知有两个内角分别是 45° 和 45°，那么这个三角形一定是？",
            options = listOf(
                "等腰直角三角形",
                "钝角三角形",
                "等边三角形",
                "锐角三角形"
            ),
            correctIndex = 0,
            explanation = "三角形内角和为 180°。第三个角 = 180° - 45° - 45° = 90°，所以是直角三角形；同时有两个内角相等均为45°，对应的两边相等，所以也是等腰三角形，合起来就是“等腰直角三角形”。",
            keyPoints = listOf("三角形内角和恒为180°", "两个底角相等的三角形为等腰三角形"),
            sampleEssence = "等腰直角三角形三内角为45°、45°、90°，具有高度轴对称性。"
        ),

        // === 4年级 英语 ===
        Question(
            id = "g4_en_1",
            grade = Grade.GRADE_4,
            subject = Subject.ENGLISH,
            title = "【现在进行时】Look! The children ______ happily on the playground now.",
            options = listOf(
                "is playing",
                "plays",
                "are playing",
                "played"
            ),
            correctIndex = 2,
            explanation = "句首有标志词 'Look!'，且时间状语为 'now'，表示动作正在发生，用现在进行时 (be + doing)。主语 'The children' 是复数名词（child的复数），因此 be动词用 are，即 are playing。",
            keyPoints = listOf("现在进行时结构：be (am/is/are) + doing", "主谓一致：children 是复数用 are"),
            sampleEssence = "标志词速记：Look, Listen, now, at the moment 常常对应现在进行时。"
        ),
        Question(
            id = "g4_en_2",
            grade = Grade.GRADE_4,
            subject = Subject.ENGLISH,
            title = "【名词辨析与句型】— ______ season do you like best? — I like autumn best because the leaves turn golden.",
            options = listOf(
                "Which",
                "Where",
                "Who",
                "When"
            ),
            correctIndex = 0,
            explanation = "提问“哪一个季节”使用疑问代词 'Which'。'Which season do you like best?' 是小学四年级最经典且必考的季节偏好句型。",
            keyPoints = listOf("Which + 名词：询问在特定范围内选择哪一个", "季节篇表达：favourite season = like best"),
            sampleEssence = "常用句型：Which season do you like best? / Why? Because..."
        ),

        // === 5年级 语文/作文 ===
        Question(
            id = "g5_cn_1",
            grade = Grade.GRADE_5,
            subject = Subject.CHINESE_ESSAY,
            title = "【人物细节描写】写《我的外公》时，哪种描写能最生动地展现外公勤劳俭朴的特点？",
            options = listOf(
                "我的外公是一个非常勤劳俭朴的老人，大家都这么夸他。",
                "外公手上布满了老茧，一件洗得泛白的旧蓝布衫上总扎着一圈工整的麻线。",
                "外公每天早起晚睡，做很多事情，特别厉害。",
                "外公总跟我们说他小时候吃了很多苦。"
            ),
            correctIndex = 1,
            explanation = "作文切忌“空洞口号”。选项B运用了精准的外貌细节与道具细节（手上的老茧、洗得泛白的旧蓝布衫、扎着的麻线），以细节刻画人物，让“勤劳俭朴”跃然纸上，远胜于其他选项的泛泛空谈。",
            keyPoints = listOf("人物描写三大法宝：动作、语言、外貌细节刻画", "作文心法：用细节说话，少用概念式标签"),
            sampleEssence = "细节秘诀：“外貌见风霜，动作显性格，物件藏真情。”"
        ),
        Question(
            id = "g5_cn_2",
            grade = Grade.GRADE_5,
            subject = Subject.CHINESE_ESSAY,
            title = "【说明文说明方法】“同茫茫宇宙相比，地球是渺小的。它只有这么大，不会再长大。”这里运用的说明方法主要是？",
            options = listOf(
                "作比较、打比方",
                "作比较、列数字",
                "作比较",
                "举例子、下定义"
            ),
            correctIndex = 2,
            explanation = "句子将“地球”与“茫茫宇宙”放在一起进行对比，突出了地球在宇宙中相对狭小的特点，运用的是典型的“作比较”说明方法。",
            keyPoints = listOf("常见说明方法：举例子、作比较、列数字、打比方、分类别", "作比较作用：通过对比突出事物本质特征"),
            sampleEssence = "五年级说明文答题公式：运用了作比较的说明方法，把A与B进行对比，突出了A...的特点。"
        ),

        // === 5年级 数学 ===
        Question(
            id = "g5_ma_1",
            grade = Grade.GRADE_5,
            subject = Subject.MATH,
            title = "【因数与倍数】下列关于质数（素数）和合数的说法中，完全正确的一项是？",
            options = listOf(
                "所有的奇数都是质数",
                "所有的偶数都是合数",
                "2是最小的质数，也是唯一的偶质数",
                "1既是质数也是合数"
            ),
            correctIndex = 2,
            explanation = "A错误（例如9、15是奇数但为合数）；B错误（2是偶数但它是质数）；D错误（1既不是质数也不是合数，它只有1个因数）；C完全正确，2是最小的质数，也是质数家族中唯一的偶数。",
            keyPoints = listOf("1既不是质数也不是合数", "2是最小的质数，也是唯一的偶质数"),
            sampleEssence = "20以内的质数口诀：二三五七和十一，十三后面是十七，十九别忘也算里。"
        ),
        Question(
            id = "g5_ma_2",
            grade = Grade.GRADE_5,
            subject = Subject.MATH,
            title = "【分数基本性质】把一个分数的分子扩大到原来的 3 倍，分母缩小到原来的 1/2，分数值会变成原来的多少倍？",
            options = listOf(
                "不变",
                "1.5倍",
                "6倍",
                "5倍"
            ),
            correctIndex = 2,
            explanation = "分子扩大到原来的 3 倍，分数值会扩大 3 倍；分母缩小到原来的 1/2，分数值反而会再扩大 2 倍。两者综合：3 × 2 = 6 倍。例如原来是 1/2，分子×3得3，分母÷2得1，变成 3/1=3，3是0.5的6倍。",
            keyPoints = listOf("分数值 = 分子 ÷ 分母", "分母越小分数值越大（成反比）"),
            sampleEssence = "分数变化规律：分子变几倍就乘几，分母缩小几倍同样要乘几。"
        ),

        // === 5年级 英语 ===
        Question(
            id = "g5_en_1",
            grade = Grade.GRADE_5,
            subject = Subject.ENGLISH,
            title = "【介词用法】I usually have breakfast ______ 7:00 ______ the morning.",
            options = listOf(
                "at; on",
                "in; at",
                "at; in",
                "on; in"
            ),
            correctIndex = 2,
            explanation = "在具体几点钟用介词 'at'（at 7:00）；在上午/下午/晚上用介词 'in'（in the morning / in the afternoon）。所以正确组合为 at; in。",
            keyPoints = listOf("时间介词口诀：at加具体钟点，in加年、月、季节与早中晚，on加具体某一天", "特例：on Monday morning 涉及具体一天用 on"),
            sampleEssence = "经典短语：at 7 o'clock, in the morning, on Friday."
        ),
        Question(
            id = "g5_en_2",
            grade = Grade.GRADE_5,
            subject = Subject.ENGLISH,
            title = "【形容词比较级】An elephant is ______ than a horse, but a cheetah runs ______ than an elephant.",
            options = listOf(
                "heavier; faster",
                "heavy; fast",
                "heaviest; fastest",
                "more heavy; more fast"
            ),
            correctIndex = 0,
            explanation = "句中两处都有标志词 'than'，均需使用形容词/副词比较级。heavy 变比较级改 y 为 i 加 er，即 heavier；fast 直接加 er，即 faster。",
            keyPoints = listOf("than 前面必须使用比较级形式", "辅音字母 + y 结尾的单音节/双音节词：变 y 为 i 再加 er"),
            sampleEssence = "比较级口诀：一分为二看两边，见到than字比一比；重读辅元辅双写，y前辅音变i记心间。"
        ),

        // === 6年级 语文/作文 ===
        Question(
            id = "g6_cn_1",
            grade = Grade.GRADE_6,
            subject = Subject.CHINESE_ESSAY,
            title = "【作文立意与构思】毕业升学作文题目为《那抹留在心头的微笑》，要写出一篇立意深刻的优秀佳作，最核心的是？",
            options = listOf(
                "描写微笑的人长得多么漂亮，牙齿有多白。",
                "列举出很多很多生活里不同的人对自己的微笑。",
                "抓住一次具体的困境或挫折，写微笑给予自己的温暖、鼓励与成长的力量，以小见大。",
                "通篇抄写赞美微笑的名人名言和歌词。"
            ),
            correctIndex = 2,
            explanation = "六年级小升初作文注重“以小见大”与“情感升华”。选项C通过典型事件（面对挫折或迷茫），刻画某个关键微笑带来的心境转变和成长启迪，切口小、情感真、立意高，是拿高分的秘诀。",
            keyPoints = listOf("以小见大：从细微生活瞬间折射人生哲理或真挚温情", "立意深刻：要有转折、有体悟、有精神成长"),
            sampleEssence = "作文升华句：“那抹微笑宛如穿透阴霾的阳光，不仅照亮了那个风雨交加的黄昏，更成为了我往后面对困境时最坚强的底气。”"
        ),
        Question(
            id = "g6_cn_2",
            grade = Grade.GRADE_6,
            subject = Subject.CHINESE_ESSAY,
            title = "【文言文文意理解】《学弈》中“虽与之俱学，弗若之矣。为是其智弗若与？曰：非然也。”说明的道理是？",
            options = listOf(
                "后一个人的智力确实比前一个人笨得多。",
                "学习效果的好坏关键在于专心致志的态度，而不在于智商的高低。",
                "下棋是一门深奥的艺术，一般人学不会。",
                "老师教棋的时候偏心，没有一视同仁。"
            ),
            correctIndex = 1,
            explanation = "两人同向弈秋学棋，一人专心致志，另一人一心以为有鸿鹄将至，结果大相径庭。反问“为是其智弗若与？曰：非然也”，鲜明地揭示出：态度决定成败，专心致志才能学有所成。",
            keyPoints = listOf("《学弈》选自《孟子·告子上》", "核心寓意：专心致志的重要性"),
            sampleEssence = "名言引用积累：“业精于勤，荒于嬉；形成良习，专一笃行。”"
        ),

        // === 6年级 数学 ===
        Question(
            id = "g6_ma_1",
            grade = Grade.GRADE_6,
            subject = Subject.MATH,
            title = "【比和比例应用】一幅地图的比例尺是 1 : 5,000,000。如果在这幅地图上量得A、B两地的距离是 4 厘米，那么两地的实际距离是？",
            options = listOf(
                "20 千米",
                "200 千米",
                "2000 千米",
                "20000 米"
            ),
            correctIndex = 1,
            explanation = "实际距离 = 图上距离 ÷ 比例尺 = 4 厘米 × 5,000,000 = 20,000,000 厘米。把厘米换算为千米：去掉5个0（1千米 = 1000米 = 100,000厘米），20,000,000 ÷ 100,000 = 200 千米。",
            keyPoints = listOf("比例尺 = 图上距离 : 实际距离", "单位换算技巧：1千米 = 100,000厘米（相差5个零）"),
            sampleEssence = "比例尺计算口诀：求实际距离用乘法，厘米变千米，向左移5位小数点。"
        ),
        Question(
            id = "g6_ma_2",
            grade = Grade.GRADE_6,
            subject = Subject.MATH,
            title = "【圆与立体几何】一个圆柱和一个圆锥等底等高，如果圆锥的体积是 18 立方分米，那么圆柱的体积是？",
            options = listOf(
                "6 立方分米",
                "18 立方分米",
                "36 立方分米",
                "54 立方分米"
            ),
            correctIndex = 3,
            explanation = "等底等高的圆柱和圆锥，圆柱的体积是圆锥体积的 3 倍（V圆柱 = Sh，V圆锥 = 1/3 Sh）。已知圆锥体积是 18，所以圆柱体积 = 18 × 3 = 54 立方分米。",
            keyPoints = listOf("等底等高圆柱体积 = 3 × 圆锥体积", "牢记体积计算公式及推导关系"),
            sampleEssence = "几何规律：等底等高时，圆锥体积是圆柱的三分之一，圆柱比圆锥多两倍。"
        ),

        // === 6年级 英语 ===
        Question(
            id = "g6_en_1",
            grade = Grade.GRADE_6,
            subject = Subject.ENGLISH,
            title = "【一般过去时】Last weekend, Tom ______ to the natural science museum and ______ many dinosaurs.",
            options = listOf(
                "go; saw",
                "went; seen",
                "went; saw",
                "goes; sees"
            ),
            correctIndex = 2,
            explanation = "时间状语 'Last weekend' 表示过去的特定时间，句子谓语动词应用一般过去时。go 的过去式是 went，see 的过去式是 saw，用 and 连接并列谓语，前后时态一致，故选 went; saw。",
            keyPoints = listOf("一般过去时标志：last weekend, yesterday, in 2020", "不规则动词过去式：go -> went, see -> saw"),
            sampleEssence = "不规则动词记忆表：go-went-gone, see-saw-seen, do-did-done."
        ),
        Question(
            id = "g6_en_2",
            grade = Grade.GRADE_6,
            subject = Subject.ENGLISH,
            title = "【综合阅读与句型】If we want to protect our Earth and environment, what should we do?",
            options = listOf(
                "We should waste more water and food.",
                "We should plant more trees and recycle plastic bottles.",
                "We can cut down more trees to build roads.",
                "We should drive cars everywhere instead of walking."
            ),
            correctIndex = 1,
            explanation = "题目问如何保护地球与环境。选项B“多植树并回收塑料瓶（plant more trees and recycle plastic bottles）”是环保的正确做法。A为浪费水粮，C为砍伐树木，D为随意开车，均不合题意。",
            keyPoints = listOf("环保情境词汇：protect, environment, plant trees, recycle", "情态动词 should 表建议后接动词原形"),
            sampleEssence = "小升初热门作文话题：Green Life / Protect the Earth."
        )
    )

    fun getQuestionsByGrade(grade: Grade): List<Question> {
        return allQuestions.filter { it.grade == grade }
    }

    fun getRandomQuiz(grade: Grade, count: Int = 3): List<Question> {
        val gradeQuestions = getQuestionsByGrade(grade)
        // Ensure subject diversity: try picking one Chinese, one Math, one English if available
        val chinese = gradeQuestions.filter { it.subject == Subject.CHINESE_ESSAY }.shuffled()
        val math = gradeQuestions.filter { it.subject == Subject.MATH }.shuffled()
        val english = gradeQuestions.filter { it.subject == Subject.ENGLISH }.shuffled()

        val list = mutableListOf<Question>()
        if (chinese.isNotEmpty()) list.add(chinese.first())
        if (math.isNotEmpty()) list.add(math.first())
        if (english.isNotEmpty()) list.add(english.first())

        // If count is higher, fill from remaining
        val remaining = gradeQuestions.filterNot { list.contains(it) }.shuffled()
        for (q in remaining) {
            if (list.size >= count) break
            list.add(q)
        }
        return list.take(count).shuffled()
    }

    fun getQuestionById(id: String): Question? {
        return allQuestions.find { it.id == id }
    }
}
