import {axe, render, screen} from '@/test-utils';
import {Logo} from '../components/logo/Logo';

describe('Logo', () => {
    axe([<Logo key="1" />]);

    it('renders correctly', () => {
        render(<Logo />);
    });

    it('renders an image with correct alt text', () => {
        render(<Logo />);
        expect(screen.getByRole('img', { name: /mitelovers logo/i })).toBeInTheDocument();
    });

    it('renders with correct height', () => {
        render(<Logo />);
        const img = screen.getByRole('img', { name: /mitelovers logo/i });
        expect(img).toHaveAttribute('height', '40');
    });

    it('renders the light logo by default', () => {
        render(<Logo />);
        const img = screen.getByRole('img', { name: /mitelovers logo/i });
        expect(img).toHaveAttribute('src', '/src/assets/MiteloversLogoBlack.svg');
    });
});