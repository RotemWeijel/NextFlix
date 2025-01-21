import { BrowserRouter } from 'react-router-dom';
import VideoPlayerDetails from '../../components/features/VideoPlayerDetails/VideoPlayerDetails'
import RecommendSeries from '../../components/features/RecommendSeries/RecommendSeries';
import DetailsMovie from '../../components/features/DetailsMovie/DetailsMovie';
import MovieFooter from '../../components/features/FooterDetailsMovie/MovieFooter';
const MovieDetailsScreen = ({ movie }) => {
    return (
        <div>
            <BrowserRouter>
                <VideoPlayerDetails />
            </BrowserRouter>
            <DetailsMovie />
            <RecommendSeries movieId={100} />
            <MovieFooter />
        </div>)
}
export default MovieDetailsScreen