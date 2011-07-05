function [optimizer, optimum, num_iterations] = simulated_annealing(cost_function, ...
        random_neighbor_function, initial_point, options)
    
    default_options.initial_temp = 1000;
    default_options.temp_update = @(T) (T * 0.999);
    default_options.stop_temp = 1e-8;
    default_options.max_iterations = 100000;

    if (~exist('options.initial_temp'))
        options.initial_temp = default_options.initial_temp;
    end
    
    if (~exist('options.temp_update'))
        options.temp_update = default_options.temp_update;
    end
    
    if (~exist('options.stop_temp'))
        options.stop_temp = default_options.stop_temp;
    end
    
    if (~exist('options.max_iterations'))
        options.max_iterations = default_options.max_iterations;
    end
    
    k = 1;
    
    T = options.initial_temp;
    current_energy = cost_function(initial_point);
    current_point = initial_point;
    previous_point = current_point;
    previous_energy = current_energy;
    
    min_energy = current_energy;
    min_point = current_point;
    
    finished = 0;
    iteration = 0;
    
    while (~finished)
        candidate_point = random_neighbor_function(current_point);
        candidate_energy = cost_function(current_point);
        
        if (candidate_energy < min_energy)
            previous_point = current_point;
            previous_energy = current_energy;
            
            current_point = candidate_point;
            current_energy = candidate_energy;
            
            min_energy = candidate_energy;
            min_point = candidate_point;
            continue;
        end
        
        if (candidate_energy < previous_energy)
            previous_point = current_point;
            previous_energy = current_energy;
            
            current_point = candidate_point;
            current_energy = candidate_energy;
        else
            if (rand() < exp(-(candidate_energy - current_energy) / (k * T)))
                previous_point = current_point;
                previous_energy = current_energy;
            
                current_point = candidate_point;
                current_energy = candidate_energy;
            else
                % current_point = current_point;
            end
        end
        
        T = options.temp_update(T);
        iteration = iteration + 1;
        
        if (iteration >= options.max_iterations)
            finished = 1;
        end
        if (T < options.stop_temp)
            finished = 1;
        end
    end
    
    num_iterations = iteration;
    
    optimizer = min_point;
    optimum = min_energy;
    
end