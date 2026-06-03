import { axe, render, screen } from '@/test-utils';
import MySalesPage from '../pages/MySales/MySalesPage';

describe('MySalesPage', () => {
    axe([<MySalesPage key="1" />]);

    it('renders correctly', () => {
        render(<MySalesPage />);
    });

    it('renders the page title', () => {
        render(<MySalesPage />);
        expect(screen.getByRole('heading', { name: /my sales/i })).toBeInTheDocument();
    });

    it('renders the sales table content', () => {
        render(<MySalesPage />);
        expect(screen.getByText('1984')).toBeInTheDocument();
    });

    it('renders the create sale button', () => {
        render(<MySalesPage />);
        expect(screen.getByRole('button', { name: /create a sale/i })).toBeInTheDocument();
    });

});