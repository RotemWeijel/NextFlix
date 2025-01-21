import { useParams, useNavigate } from 'react-router-dom';
import { useEffect } from 'react'
import VideoPlayerDetails from '../../components/features/VideoPlayerDetails/VideoPlayerDetails'
import RecommendSeries from '../../components/features/RecommendSeries/RecommendSeries';
import DetailsMovie from '../../components/features/DetailsMovie/DetailsMovie';
import MovieFooter from '../../components/features/FooterDetailsMovie/MovieFooter';
const MovieDetailsScreen = ({ }) => {
    const movieId = useParams()
    const navigate = useNavigate();
    useEffect(() => {
        if (!movieId) {
            navigate('/');
            return;
        }
    }, [movieId]);
    return (
        <div>
            <VideoPlayerDetails />
            <DetailsMovie />
            <RecommendSeries movieId={100} />
            <MovieFooter />
        </div>)
}
export default MovieDetailsScreen