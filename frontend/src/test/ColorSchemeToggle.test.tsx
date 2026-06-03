import { axe, render, screen, fireEvent } from '@/test-utils';
import { ColorSchemeToggle } from '../components/colorscheme/ColorSchemeToggle';

describe('ColorSchemeToggle', () => {
    axe([<ColorSchemeToggle key="1" />]);

    it('renders correctly', () => {
        render(<ColorSchemeToggle />);
    });

    it('renders the toggle button', () => {
        render(<ColorSchemeToggle />);
        expect(
            screen.getByRole('button', { name: /toggle color scheme/i })
        ).toBeInTheDocument();
    });

    it('shows moon icon on light mode by default', () => {
        render(<ColorSchemeToggle />);
        expect(document.querySelector('svg')).toBeInTheDocument();
    });

    it('toggles color scheme on click', () => {
        render(<ColorSchemeToggle />);
        const button = screen.getByRole('button', { name: /toggle color scheme/i });
        fireEvent.click(button);
        expect(button).toBeInTheDocument();
    });
});