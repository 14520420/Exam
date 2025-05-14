package tool;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Teacher;

@WebServlet(urlPatterns = { "*.action" })
public class FrontController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            // パスを取得
            String path = req.getServletPath().substring(1);

            // ログイン認証チェック - ログイン関連のアクション以外は認証が必要
            if (!path.startsWith("scoremanager/Login") && !path.equals("scoremanager/LoginExecute.action")) {
                HttpSession session = req.getSession();
                Teacher user = (Teacher) session.getAttribute("user");

                if (user == null || !user.isAuthenticated()) {
                    // 認証されていない場合、ログインページにリダイレクト
                    res.sendRedirect("/exam/scoremanager/Login.action");
                    return;
                }

                // 管理者権限チェック - 教員管理は管理者のみアクセス可能
                if (path.contains("Teacher") && !path.equals("scoremanager/main/Logout.action")) {
                    if (!user.isAdmin()) {
                        req.setAttribute("error", "この機能へのアクセス権限がありません。");
                        req.getRequestDispatcher("/error.jsp").forward(req, res);
                        return;
                    }
                }
            }

            // ファイル名を取得しクラス名に変換
            String name = path.replace(".a", "A").replace('/', '.');

            // アクションクラスのインスタンスを返却
            Action action = (Action) Class.forName(name).getDeclaredConstructor().newInstance();

            // アクションを実行
            action.execute(req, res);

        } catch (ClassNotFoundException e) {
            // クラスが見つからない場合のエラー処理
            req.setAttribute("error", "リクエストされたアクションが見つかりません。");
            req.getRequestDispatcher("/error.jsp").forward(req, res);
        } catch (Exception e) {
            e.printStackTrace();

            // エラーの種類に応じたユーザーフレンドリーなメッセージを設定
            String errorMessage = "システムエラーが発生しました。";
            if (e.getMessage() != null && !e.getMessage().isEmpty()) {
                errorMessage = e.getMessage();
            }

            req.setAttribute("error", errorMessage);
            req.getRequestDispatcher("/error.jsp").forward(req, res);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doGet(req, res);
    }
} 