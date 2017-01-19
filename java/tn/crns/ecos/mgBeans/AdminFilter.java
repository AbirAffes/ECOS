package tn.crns.ecos.mgBeans;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter("/pages/admin/adminhome.do")
public class AdminFilter implements Filter{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
	
			HttpServletRequest req = (HttpServletRequest) request;
			HttpServletResponse resp = (HttpServletResponse) response;
			IdentiteBean identite = (IdentiteBean) req.getSession().getAttribute("identity");
			Boolean letGo = false;
			if (identite!=null &&
					identite.getUtilisateur()!=null &&
						(identite.hasRole("AdminA")||identite.hasRole("AdminEn")||identite.hasRole("AdminEt")||identite.hasRole("AdminEx"))
			) {
				letGo = true;
			}
			if(letGo){
				chain.doFilter(request, response);
			}else{
				resp.sendRedirect(req.getContextPath() + "/pages/auth/accueil.do");
			}
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
}
