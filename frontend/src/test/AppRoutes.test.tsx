import { axe, render, screen } from '@/test-utils';
import { AppRoutes } from '../routes/AppRoutes';

describe('AppRoutes', () => {
    axe([<AppRoutes key="1" />]);

    it('renders correctly', () => {
        render(<AppRoutes />);
    });

    it('renders Marketplace on default route', () => {
        render(<AppRoutes />);
        expect(screen.getByRole('heading', { name: /marketplace/i })).toBeInTheDocument();
    });

    it('renders MyListsPage on /my-lists route', () => {
        render(<AppRoutes />, { initialEntries: ['/my-lists'] });
        expect(screen.getByRole('heading', { name: /my lists/i })).toBeInTheDocument();
    });

    it('renders MyLibraryPage on /my-library route', () => {
        render(<AppRoutes />, { initialEntries: ['/my-library'] });
        expect(screen.getByRole('heading', { name: /my library/i })).toBeInTheDocument();
    });
});