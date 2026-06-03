import { axe, render, screen } from '@/test-utils';
import { Footer } from '../components/footer/Footer';

describe('Footer', () => {
    axe([<Footer key="1" />]);

    it('renders correctly', () => {
        render(<Footer />);
    });

    it('displays the copyright text', () => {
        render(<Footer />);
        expect(
            screen.getByText('© 2026 Cooperativa de Código da Asprela')
        ).toBeInTheDocument();
    });
});