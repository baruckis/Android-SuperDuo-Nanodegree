package barqsoft.footballscores;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yehya khaled on 3/3/2015.
 */
public class Utilies
{
    public static final int SERIE_A = 357;
    public static final int PREMIER_LEGAUE = 354;
    public static final int CHAMPIONS_LEAGUE = 362;
    public static final int PRIMERA_DIVISION = 358;
    public static final int BUNDESLIGA = 351;

    public static String getLeague(Context context, int league_num)
    {
        switch (league_num)
        {
            case SERIE_A : return context.getString(R.string.league_serie_a_name);
            case PREMIER_LEGAUE : return context.getString(R.string.league_premier_name);
            case CHAMPIONS_LEAGUE : return context.getString(R.string.league_uefa_champions_name);
            case PRIMERA_DIVISION : return context.getString(R.string.league_primera_division_name);
            case BUNDESLIGA : return context.getString(R.string.league_bundesliga_name);
            default: return context.getString(R.string.league_name_not_known_msg);
        }
    }
    public static String getMatchDay(Context context, int match_day, int league_num)
    {
        if(league_num == CHAMPIONS_LEAGUE)
        {
            if (match_day <= 6)
            {
                return context.getString(R.string.match_day_group_stages_msg);
            }
            else if(match_day == 7 || match_day == 8)
            {
                return context.getString(R.string.match_day_first_knockout_msg);
            }
            else if(match_day == 9 || match_day == 10)
            {
                return context.getString(R.string.match_day_quarterfinal_msg);
            }
            else if(match_day == 11 || match_day == 12)
            {
                return context.getString(R.string.match_day_semifinal_msg);
            }
            else
            {
                return context.getString(R.string.match_day_final_msg);
            }
        }
        else
        {
            return context.getString(R.string.match_day_msg) + String.valueOf(match_day);
        }
    }

    public static String getScores(Context context, int home_goals, int awaygoals)
    {
        if(home_goals < 0 || awaygoals < 0)
        {
            return context.getString(R.string.scores_separator);
        }
        else
        {
            return String.valueOf(home_goals) + context.getString(R.string.scores_separator) + String.valueOf(awaygoals);
        }
    }

    public static int getTeamCrestByTeamName (Context context, String teamname)
    {
        if (teamname==null){return R.drawable.no_icon;}

        //This is the set of icons that are currently in the app. Feel free to find and add more
        //as you go.
        if (teamname.equals(context.getString(R.string.team_arsenal_london_fc_name))) {
            return R.drawable.arsenal;
        } else if (teamname.equals(context.getString(R.string.team_manchester_united_fc_name))) {
            return R.drawable.manchester_united;
        } else if (teamname.equals(context.getString(R.string.team_swansea_city_name))) {
            return R.drawable.swansea_city_afc;
        } else if (teamname.equals(context.getString(R.string.team_leicester_city_name))) {
            return R.drawable.leicester_city_fc_hd_logo;
        } else if (teamname.equals(context.getString(R.string.team_everton_fc_name))) {
            return R.drawable.everton_fc_logo1;
        } else if (teamname.equals(context.getString(R.string.team_west_ham_united_fc_name))) {
            return R.drawable.west_ham;
        } else if (teamname.equals(context.getString(R.string.team_tottenham_hotspur_fc_name))) {
            return R.drawable.tottenham_hotspur;
        } else if (teamname.equals(context.getString(R.string.team_west_bromwich_albion_name))) {
            return R.drawable.west_bromwich_albion_hd_logo;
        } else if (teamname.equals(context.getString(R.string.team_sunderland_afc_name))) {
            return R.drawable.sunderland;
        } else if (teamname.equals(context.getString(R.string.team_stoke_city_fc_name))) {
            return R.drawable.stoke_city;
        } else {
            return R.drawable.no_icon;
        }
    }

    public static String getFragmentDate(Context context, int offSet) {
        Date fragmentdate = new Date(System.currentTimeMillis() + (offSet * 86400000));
        SimpleDateFormat mformat = new SimpleDateFormat(context.getString(R.string.date_pattern));
        return mformat.format(fragmentdate);
    }
}
