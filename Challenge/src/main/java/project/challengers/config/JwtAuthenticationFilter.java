package project.challengers.config;

public class JwtAuthenticationFilter {

//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        // 헤더에서 JWT 를 가져옴
//        String token = jwtTokenComp.resolveToken((HttpServletRequest) request);
//
//        // 유효한 토큰인지 확인
//        if (token != null && token.startsWith("Bearer ")) {
//            token = token.substring(7);
//            if (jwtTokenComp.validateToken(token)) {
//                // 토큰이 유효하면 토큰으로부터 유저 정보를 받아옴
//                Authentication authentication = jwtTokenComp.getAuthentication(token);
//                // SecurityContext 에 Authentication 객체를 저장
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }
//        }
//        chain.doFilter(request, response);
//    }
}
