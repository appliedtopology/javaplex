function [minimizer, minimum] = greedy_search(f, initial_point)
    current_point = initial_point;
    previous_point = current_point;
    current_value = f(initial_point);
    previous_value = current_value;
    
    k = length(initial_point);
    
    while 1
        
        for i = 1:k
            current_point(i) = 1 - current_point(i);
            current_value = f(current_point);
            if (current_value < previous_value)
                break;
            end
            current_point(i) = 1 - current_point(i);
            
            if (i == k)
                minimizer = previous_point;
                minimum = previous_value;
                return;
            end
              
        end
        
        if (current_value >= previous_value)
            break;
        end
        
        previous_point = current_point;
        previous_value = current_value;
    end
    
    minimizer = current_point;
    minimum = current_value;
end